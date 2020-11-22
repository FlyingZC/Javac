package com.sun.tools.javac.zc.zc06.t03;

class Test {
    int a = 1;

    static {
        // 报错，无法从静态上下文中引用非静态变量a
        // 调用Resolve类的相关方法将返回StaticError对象。如果删除定义变量a的语句，
        // Resolve类的相关方法将返回SymbolNotFoundError对象，表示无法找到对应的符号。
        // int b = a;
    }
}