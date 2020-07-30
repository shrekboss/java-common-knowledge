package org.bytedancer.crayzer.common_dev_error.coding.concurrentTools.test;

import java.util.concurrent.CountDownLatch;

public class MyThreadLocalStringDemo {

    private static ThreadLocal<String> threadLocal = new ThreadLocal<>();

    private String getString() {
        return threadLocal.get();
    }

    private void setString(String string) {
        threadLocal.set(string);
    }

    public static void main(String[] args) {
        MyThreadLocalStringDemo demo = new MyThreadLocalStringDemo();
        CountDownLatch countDownLatch = new CountDownLatch(9);
        for (int i = 0; i < 9; i++) {
            Thread thread = new Thread(() -> {
                demo.setString(Thread.currentThread().getName());
                System.out.println(demo.getString());
                countDownLatch.countDown();
            },"thread - " + i);
            thread.start();
        }
    }
}
