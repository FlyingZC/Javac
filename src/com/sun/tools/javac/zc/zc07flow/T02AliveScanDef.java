package com.sun.tools.javac.zc.zc07flow;

/**
 * @author zhangcheng
 * @date 2020/11/21
 */
public class T02AliveScanDef {
    {
        throw new RuntimeException(); // 报错,初始化程序必须能够正常完成
    }
}
