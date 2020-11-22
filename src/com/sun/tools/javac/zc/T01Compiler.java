package com.sun.tools.javac.zc;

import javax.tools.ToolProvider;
import java.io.IOException;

public class T01Compiler {
    private static final String BASE_DIR = "/Users/duandian/Documents/0zc/02-my/00-code/03-opensource/Javac/src/com/sun/tools/javac/zc/";

    public static void main(String args[]) throws IOException {

        String path1 = BASE_DIR + "/HelloWorld.java";
        String path2 = BASE_DIR + "/z02tree/T01ClassType.java";
        String path3 = BASE_DIR + "/z03symbol/TestOuterProperty.java";
        String path4 = BASE_DIR + "/z03symbol/enter/TestOuterA.java";
        String path5 = BASE_DIR + "/z05/T01Converter.java";
        String path6 = BASE_DIR + "/z05semantic/Test.java";

        String path = path1;

        javax.tools.JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        int result = compiler.run(null, null, null,
                new String[]{
                        "-d", "/Users/duandian/Documents/0zc/02-my/00-code/03-opensource/Javac/src/com/sun/tools/javac/zc/",
                        path
                } // 传入 arguments 参数数组
                // javac -d C:/javacclass C:/JavaCompiler/test/chapter1/TestJavac.java
                // -d 表示生成的 class文件的存放路径
        );
        System.out.println("Result code: " + result);
    }
}