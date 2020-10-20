package com.sun.tools.javac.zc.z02tree;

/**
 * @author zhangcheng
 * @date 2020/10/18
 */
public class TestIf {
    public static void main(String[] args) {
        if (true) {
            if (false) {

            }
        } else {
            System.out.println("ab");
        }

// 悬空if
//        if (res1)
//            if (res2) {
//                // ...
//            } else {
//                // ...
//            }
    }
}
