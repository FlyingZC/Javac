package com.sun.tools.javac.zc;

import javax.tools.ToolProvider;
import java.io.IOException;

public class T01Compiler {
    public static void main(String args[]) throws IOException {
        String path = "/Users/duandian/Documents/0zc/02-my/00-code/03-opensource/openjdk/langtools/src/share/classes/com/sun/tools/javac/zc/HelloWorld.java";
        javax.tools.JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        int result = compiler.run(null, null, null,
                new String[]{
                        "-d", "/Users/duandian/Documents/0zc/02-my/00-code/03-opensource/openjdk/langtools/src/share/classes/com/sun/tools/javac/zc",
                        path
                } // 传入 arguments 参数数组
                // javac -d C:/javacclass C:/JavaCompiler/test/chapter1/TestJavac.java
                // -d 表示生成的 class文件的存放路径
        );
        System.out.println("Result code: " + result);
    }
}