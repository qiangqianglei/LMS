package com.lms.demo.designmode;

public class Singleton {
    private static Singleton instance;
    private Singleton () {

    }

    private static Singleton getInstance() {
        if (instance == null) {
            instance = new Singleton();
        }
        return instance;
    }

    private static synchronized Singleton getInstance1() {
        if (instance == null) {
            instance = new Singleton();
        }
        return instance;
    }


    private static synchronized Singleton getInstance2() {
        //第一次判断，如果instance不为null，不进入抢锁阶段，直接返回实例
        if (instance == null) {
            synchronized (Singleton.class) {
                // 抢到锁之后再次判断是否为null
                if (instance == null) {
                    instance = new Singleton();
                }
            }
        }
        return instance;
    }
}
