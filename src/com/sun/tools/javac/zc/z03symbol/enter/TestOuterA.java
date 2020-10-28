package com.sun.tools.javac.zc.z03symbol.enter;

/**
 * Enter.uncompleted 会保存
 * com.sun.tools.javac.zc.z03symbol.enter.TestOuterA.TestA
 * com.sun.tools.javac.zc.z03symbol.enter.TestOuterA.TestA.TestB
 * com.sun.tools.javac.zc.z03symbol.enter.TestOuterB
 * md()方法里的本地类 LocalA 不会保存到这里
 */
public class TestOuterA {
    class TestA {
        class TestB {
        }
    }

    public void md() {
        class LocalA {
        }
    }
}

class TestOuterB {
}