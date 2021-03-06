/*
 * Copyright (c) 1999, 2012, Oracle and/or its affiliates. All rights reserved.
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

package com.sun.tools.javac.code;

import com.sun.source.tree.Tree.Kind;

import javax.lang.model.type.TypeKind;

/** An interface for type tag values, which distinguish between different
 *  sorts of types. Java不同类型的标记
 *
 *  <p><b>This is NOT part of any supported API.
 *  If you write code that depends on this, you do so at your own risk.
 *  This code and its internal interfaces are subject to change or
 *  deletion without notice.</b>
 */
public enum TypeTag {
    /** The tag of the basic type `byte'.
     */
    BYTE(1),

    /** The tag of the basic type `char'.
     */
    CHAR(2),

    /** The tag of the basic type `short'.
     */
    SHORT(3),

    /** The tag of the basic type `int'.
     */
    INT(4),

    /** The tag of the basic type `long'.
     */
    LONG(5),

    /** The tag of the basic type `float'.
     */
    FLOAT(6),

    /** The tag of the basic type `double'.
     */
    DOUBLE(7),

    /** The tag of the basic type `boolean'.
     */
    BOOLEAN,

    /** The tag of the type `void'.
     */
    VOID,

    /** The tag of all class and interface types.
     */
    CLASS,

    /** The tag of all array types.
     */
    ARRAY,

    /** The tag of all (monomorphic) method types.
     */
    METHOD,

    /** The tag of all package "types".
     */
    PACKAGE,

    /** The tag of all (source-level) type variables.
     */
    TYPEVAR,

    /** The tag of all type arguments.
     */
    WILDCARD,

    /** The tag of all polymorphic (method-) types.
     */
    FORALL,

    /** The tag of deferred expression types in method context
     */
    DEFERRED,

    /** The tag of the bottom type {@code <null>}.
     */
    BOT,

    /** The tag of a missing type.
     */
    NONE,

    /** The tag of the error type.
     */
    ERROR,

    /** The tag of an unknown type
     */
    UNKNOWN,

    /** The tag of all instantiatable type variables.
     */
    UNDETVAR,

    /** Pseudo-types, these are special tags
     */
    UNINITIALIZED_THIS,

    UNINITIALIZED_OBJECT;

    /** This field will only be used for tags related with numeric types for
     *  optimization reasons.
     */
    private final int order;

    private TypeTag() {
        this(0);
    }

    private TypeTag(int order) {
        this.order = order;
    }

    private static final int MIN_NUMERIC_TAG_ORDER = 1;
    private static final int MAX_NUMERIC_TAG_ORDER = 7;

    /** Returns the number of type tags.
     */
    public static int getTypeTagCount() {
        // last two tags are not included in the total as long as they are pseudo-types
        return (UNDETVAR.ordinal() + 1);
    }

    public boolean isSubRangeOf(TypeTag range) {
        return (this == range) || isStrictSubRangeOf(range);
    }

    public boolean isStrictSubRangeOf(TypeTag range) {
        if (this.order >= MIN_NUMERIC_TAG_ORDER && this.order <= MAX_NUMERIC_TAG_ORDER &&
            range.order >= MIN_NUMERIC_TAG_ORDER && this.order <= MAX_NUMERIC_TAG_ORDER) {
            if (this == range)
                return false;
            switch (this) {
                case BYTE:
                    return true;
                case CHAR: case SHORT: case INT:
                case LONG: case FLOAT:
                    return this.order < range.order && range.order <= MAX_NUMERIC_TAG_ORDER;
                default:
                    return false;
            }
        }
        else
            return false;
    }

    public Kind getKindLiteral() {
        switch (this) {
        case INT:
            return Kind.INT_LITERAL;
        case LONG:
            return Kind.LONG_LITERAL;
        case FLOAT:
            return Kind.FLOAT_LITERAL;
        case DOUBLE:
            return Kind.DOUBLE_LITERAL;
        case BOOLEAN:
            return Kind.BOOLEAN_LITERAL;
        case CHAR:
            return Kind.CHAR_LITERAL;
        case CLASS:
            return Kind.STRING_LITERAL;
        case BOT:
            return Kind.NULL_LITERAL;
        default:
            throw new AssertionError("unknown literal kind " + this);
        }
    }

    public TypeKind getPrimitiveTypeKind() {
        switch (this) {
        case BOOLEAN:
            return TypeKind.BOOLEAN;
        case BYTE:
            return TypeKind.BYTE;
        case SHORT:
            return TypeKind.SHORT;
        case INT:
            return TypeKind.INT;
        case LONG:
            return TypeKind.LONG;
        case CHAR:
            return TypeKind.CHAR;
        case FLOAT:
            return TypeKind.FLOAT;
        case DOUBLE:
            return TypeKind.DOUBLE;
        case VOID:
            return TypeKind.VOID;
        default:
            throw new AssertionError("unknown primitive type " + this);
        }
    }
}
