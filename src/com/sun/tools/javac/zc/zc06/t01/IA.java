package com.sun.tools.javac.zc.zc06.t01;

interface IA {
    int a = 1;
}

interface IB {
    int a = 2;
}

class CA implements IA, IB {
    // 报错，对a的引用不明确, IA中的变量 a和IB中的变量 a都匹配
    // 调用Resolve类中相关的方法查找a的引用时会返回AmbiguitError对象
    // int b = a;
}