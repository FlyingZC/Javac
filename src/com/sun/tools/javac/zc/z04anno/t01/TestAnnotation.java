package com.sun.tools.javac.zc.z04anno.t01;

// 使用自定义的 Getter 注解
// 会为TestAnnotation的两个变量name与age生成getName()与getAge()方法
@MyGetter
public class TestAnnotation {
    private String name;
    private int age;
}