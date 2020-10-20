package com.sun.tools.javac.zc.z02tree;

/**
 * @author zhangcheng
 * @date 2020/10/18
 */
public class TestJCVariableDecl {
    public static void main(String[] args) {
        int a = 1, b = a;

        int a1 = 1;
        int b1 = a;
    }
}
