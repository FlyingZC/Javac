package com.sun.tools.javac.zc.z04anno.t01;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// 自定义注解处理器处理的Getter注解类
@Target({ElementType.TYPE}) // 当前的注解类型只能标注在类型上
@Retention(RetentionPolicy.SOURCE) // 此注解类型只在编译Java源代码时有效,在生成Class文件过程中将会被Javac抛弃
public @interface MyGetter {
}