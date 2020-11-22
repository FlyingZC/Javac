package com.sun.tools.javac.zc.z04anno.t01;

import javax.tools.ToolProvider;

// 指定GetSetProcessor注解处理器处理TestAnnotation类
public class Test {
    public static void main(String[] args) {
        // 通过javap-verbose TestAnnotation命令进行反编译后可以看到，确实为TestAnnotation类生成了getName()与getAge()方法
        javax.tools.JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        int results = compiler.run(null, null, null, new String[]{
                "-processor", "com.sun.tools.javac.zc.z04anno.t01.GetSetProcessor", // -processor命令指定具体的注解处理器类
                "-processorpath", "/Users/duandian/Documents/0zc/02-my/00-code/03-opensource/Javac/src/com/sun/tools/javac/zc/z04anno", // -processorpath命令 指定搜索注解处理器的路径
                "-d", "/Users/duandian/Documents/0zc/02-my/00-code/03-opensource/Javac/src/com/sun/tools/javac/zc/z04anno", // 运行后会在指定路径下生成TestAnnotation.class类
                "/Users/duandian/Documents/0zc/02-my/00-code/03-opensource/Javac/src/com/sun/tools/javac/zc/z04anno/TestAnnotation.java"
        });
    }
}
