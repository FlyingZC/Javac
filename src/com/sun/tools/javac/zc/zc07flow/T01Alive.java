package com.sun.tools.javac.zc.zc07flow;

/**
 * @author zhangcheng
 * @date 2020/11/21
 */
public class T01Alive {
    public void test() {
        return;
        System.out.println("unreachable statement"); // 报错,无法访问的语句
    }

    public void test2() {
        while (true) {
        }
        System.out.println("unreachable statement"); // 报错,无法访问的语句
    }

    public void test(boolean res) throws Exception {
        if (res) {
            throw new Exception();
        } else {
            throw new Exception();
        }
        System.out.println("unreachable statement");// 报错，无法访问的语句
    }



}
