package com.sun.tools.javac.zc.z02tree;

/**
 * @author zhangcheng
 * @date 2020/10/18
 */
public class TestJCLiteral {
    public static void main(String[] args) {
        // 下面所有变量的初始化部分都是一个JCLiteral对象
        int a = 1;
        long b = 2L;
        float c = 3f;
        double d = 4d;
        Object e = null;
        String f = "aa";
    }
}
