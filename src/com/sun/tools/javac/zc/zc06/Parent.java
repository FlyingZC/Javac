package com.sun.tools.javac.zc.zc06;

class Parent {
    int a = 1;
}

class Sub extends Parent {
    int a = 2;

    public void md() {
        int a = 3;
        int b = a; // 使用局部变量a的值进行初始化
    }
}