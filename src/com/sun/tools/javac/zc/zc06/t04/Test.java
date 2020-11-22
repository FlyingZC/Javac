package com.sun.tools.javac.zc.zc06.t04;

/**
 * JCIdent树节点的标注
 * 变量b的初始化部分是一个名称为a的JCIdent树节点，引用了成员变量a；
 * 变量c声明的是一个名称为Test的JCIdent树节点，引用了Test类；
 */
public class Test {
    public int a = 1;

    public void md() {
    }

    public void test() {
        int b = a;
        Test c = new Test();
        md();
    }
}