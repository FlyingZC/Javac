package com.sun.tools.javac.zc.z04anno.t02;

import javax.tools.ToolProvider;
import java.io.IOException;

public class RunNameCheckProcessorMain {
   public static void main(String args[]) throws IOException {
      javax.tools.JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
      int results = compiler.run(null, null, null, new String[]{
            "-processor","com.sun.tools.javac.zc.z04anno.t02.NameCheckProcessor",
            "-processorpath","/Users/duandian/Documents/0zc/02-my/00-code/03-opensource/Javac/src/com/sun/tools/javac/zc/z04anno/t02",
            "-d","/Users/duandian/Documents/0zc/02-my/00-code/03-opensource/Javac/src/com/sun/tools/javac/zc/z04anno/t02",
            "/Users/duandian/Documents/0zc/02-my/00-code/03-opensource/Javac/src/com/sun/tools/javac/zc/z04anno/t02/TEST.java"
      });
      System.out.println("Result code: " + results);
   }
}