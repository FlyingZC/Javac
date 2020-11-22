package com.sun.tools.javac.zc.z05semantic;

class Test {
//    class MemberClassA extends MemberClassB {
//    }
//
//    class MemberClassB {
//    }

    public void test() {

        // 报错，找不到符号
        // LocalClassA继承LocalClassB时会报错
        // 因为LocalClassB是本地类并且定义在LocalClassA之后
        // 所以如果使用在块内的定义,则定义必须在使用之前
        class LocalClassA extends LocalClassB {
        }

        class LocalClassB {
        }
    }
}