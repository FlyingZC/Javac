package com.sun.tools.javac.zc.z03symbol;

interface IA {
    public void method();
}

class CA {
    public void method() {
    }
}

class CB extends CA implements IA {
}

class Node<T extends String> {
    public void getVal(T t){ }
}

class MyNode extends Node implements IA{
    @Override
    public void method() {

    }
}