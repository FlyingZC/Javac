package com.sun.tools.javac.zc.ch02;

import java.util.List;

/**
 * 在编译TestJFM类时，由于使用了java.util.List类，因而需要加载List类。
 */
public class TestJFM {
    List<String> l;
}