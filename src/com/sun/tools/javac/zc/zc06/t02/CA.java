package com.sun.tools.javac.zc.zc06.t02;

class CA {
    private int a = 1;
}
class CB extends CA {
    // 报错，a可以在CA中访问private
    // 在查找a的具体引用时，判断父类CA中定义的变量a没有权限获取，返回AccessError对象
    // int b = a;
}