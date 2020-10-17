/*
 * Copyright (c) 2011, 2013, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

package com.sun.tools.javac.parser;

import java.nio.CharBuffer;
import java.util.Arrays;

import com.sun.tools.javac.file.JavacFileManager;
import com.sun.tools.javac.util.ArrayUtils;
import com.sun.tools.javac.util.Log;
import com.sun.tools.javac.util.Name;
import com.sun.tools.javac.util.Names;

import static com.sun.tools.javac.util.LayoutCharacters.*;

/** The char reader used by the javac lexer/tokenizer. Returns the sequence of
 * characters contained in the input stream, handling unicode escape accordingly.
 * Additionally, it provides features for saving chars into a buffer and to retrieve
 * them at a later stage.
 *
 *  <p><b>This is NOT part of any supported API.
 *  If you write code that depends on this, you do so at your own risk.
 *  This code and its internal interfaces are subject to change or
 *  deletion without notice.</b>
 */
public class UnicodeReader {

    /** The input buffer, index of next character to be read,
     *  index of one past last character in buffer. 字符数组,保存输入的buf,比如整个HelloWorld.java文件的字符数组.最后一个数组元素的值为EOI,表示已经没有可读取的字符
     */
    protected char[] buf;
    protected int bp; // 记录当前读取的buf数组里的字符下标,从-1开始
    protected final int buflen; // buf的长度,也就是Java源文件字符的长度

    /** The current character.保存当前从buf数组里读取出来的字符
     */
    protected char ch;

    /** The buffer index of the last converted unicode character
     */
    protected int unicodeConversionBp = -1;

    protected Log log;
    protected Names names;

    /** A character buffer for saved chars.保存当前读取的字符数组,临时用来存储从buf数组中读出来的若干个字符
     */
    protected char[] sbuf = new char[128];
    protected int sp; // sbuf的当前下标

    /**
     * Create a scanner from the input array.  This method might
     * modify the array.  To avoid copying the input array, ensure
     * that {@code inputLength < input.length} or
     * {@code input[input.length -1]} is a white space character.
     *
     * @param sf the factory which created this Scanner
     * @param buffer the input, might be modified
     * Must be positive and less than or equal to input.length.
     */
    protected UnicodeReader(ScannerFactory sf, CharBuffer buffer) {
        this(sf, JavacFileManager.toArray(buffer), buffer.limit());
    }

    protected UnicodeReader(ScannerFactory sf, char[] input, int inputLength) {
        log = sf.log;
        names = sf.names;
        if (inputLength == input.length) {
            if (input.length > 0 && Character.isWhitespace(input[input.length - 1])) {
                inputLength--;
            } else {
                input = Arrays.copyOf(input, inputLength + 1);
            }
        }
        buf = input; // 将Java源文件的字符数组保存到 buf 中
        buflen = inputLength;
        buf[buflen] = EOI; // buf 最后一位添加 EOI 结束标识
        bp = -1;
        scanChar();
    }

    /** Read next character. 从保存Java源文件的buf数组中读取一个字符
     */
    protected void scanChar() {
        if (bp < buflen) {
            ch = buf[++bp]; // 从buf数组中读取出字符 ch
            if (ch == '\\') {
                convertUnicode();
            }
        }
    }

    /** Read next character in comment, skipping over double '\' characters.
     */
    protected void scanCommentChar() {
        scanChar();
        if (ch == '\\') {
            if (peekChar() == '\\' && !isUnicode()) {
                skipChar();
            } else {
                convertUnicode();
            }
        }
    }

    /** Append a character to sbuf.
     */
    protected void putChar(char ch, boolean scan) {
        sbuf = ArrayUtils.ensureCapacity(sbuf, sp); // 自动扩容
        sbuf[sp++] = ch; // 将字符保存到 sbuf 数组中
        if (scan) // 是否继续读取字符
            scanChar(); // 接着从保存Java源文件字符的buf字符数组中读取一个字符
    }

    protected void putChar(char ch) {
        putChar(ch, false);
    }
    // 保存当前字符到 sbuf里,并继续读取 buf 数组里的字符
    protected void putChar(boolean scan) {
        putChar(ch, scan);
    }

    Name name() {
        return names.fromChars(sbuf, 0, sp); // 获取sbuf里暂存的所有字符
    }

    String chars() {
        return new String(sbuf, 0, sp);
    }

    /** Convert unicode escape; bp points to initial '\' character
     *  (Spec 3.3).
     */
    protected void convertUnicode() {
        if (ch == '\\' && unicodeConversionBp != bp) {
            bp++; ch = buf[bp];
            if (ch == 'u') {
                do {
                    bp++; ch = buf[bp];
                } while (ch == 'u');
                int limit = bp + 3;
                if (limit < buflen) {
                    int d = digit(bp, 16);
                    int code = d;
                    while (bp < limit && d >= 0) {
                        bp++; ch = buf[bp];
                        d = digit(bp, 16);
                        code = (code << 4) + d;
                    }
                    if (d >= 0) {
                        ch = (char)code;
                        unicodeConversionBp = bp;
                        return;
                    }
                }
                log.error(bp, "illegal.unicode.esc");
            } else {
                bp--;
                ch = '\\';
            }
        }
    }

    /** Are surrogates supported?
     */
    final static boolean surrogatesSupported = surrogatesSupported();
    private static boolean surrogatesSupported() {
        try {
            Character.isHighSurrogate('a');
            return true;
        } catch (NoSuchMethodError ex) {
            return false;
        }
    }

    /** Scan surrogate pairs.  If 'ch' is a high surrogate and
     *  the next character is a low surrogate, then put the low
     *  surrogate in 'ch', and return the high surrogate.
     *  otherwise, just return 0.
     */
    protected char scanSurrogates() {
        if (surrogatesSupported && Character.isHighSurrogate(ch)) {
            char high = ch;

            scanChar();

            if (Character.isLowSurrogate(ch)) {
                return high;
            }

            ch = high;
        }

        return 0;
    }

    /** Convert an ASCII digit from its base (8, 10, or 16)
     *  to its value.
     */
    protected int digit(int pos, int base) {
        char c = ch;
        int result = Character.digit(c, base);
        if (result >= 0 && c > 0x7f) {
            log.error(pos + 1, "illegal.nonascii.digit");
            ch = "0123456789abcdef".charAt(result);
        }
        return result;
    }

    protected boolean isUnicode() {
        return unicodeConversionBp == bp;
    }

    protected void skipChar() {
        bp++;
    }

    protected char peekChar() {
        return buf[bp + 1];
    }

    /**
     * Returns a copy of the input buffer, up to its inputLength.
     * Unicode escape sequences are not translated.
     */
    public char[] getRawCharacters() {
        char[] chars = new char[buflen];
        System.arraycopy(buf, 0, chars, 0, buflen);
        return chars;
    }

    /**
     * Returns a copy of a character array subset of the input buffer.
     * The returned array begins at the {@code beginIndex} and
     * extends to the character at index {@code endIndex - 1}.
     * Thus the length of the substring is {@code endIndex-beginIndex}.
     * This behavior is like
     * {@code String.substring(beginIndex, endIndex)}.
     * Unicode escape sequences are not translated.
     *
     * @param beginIndex the beginning index, inclusive.
     * @param endIndex the ending index, exclusive.
     * @throws ArrayIndexOutOfBoundsException if either offset is outside of the
     *         array bounds
     */
    public char[] getRawCharacters(int beginIndex, int endIndex) {
        int length = endIndex - beginIndex;
        char[] chars = new char[length];
        System.arraycopy(buf, beginIndex, chars, 0, length);
        return chars;
    }
}
