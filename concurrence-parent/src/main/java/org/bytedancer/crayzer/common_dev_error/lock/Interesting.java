package org.bytedancer.crayzer.common.dev.err.threadsafe.lock;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Interesting {

    volatile int a = 1;
    volatile int b = 1;

    public static void main(String[] args) {
        Interesting interesting = new Interesting();
        new Thread(() -> interesting.add()).start();
        new Thread(() -> interesting.compare()).start();
    }

    /**
     * 这个案例中的 add 方法始终只有一个线程在操作，显然只为 add 方法加锁是没用的。
     * public synchronized void add()
     *
     * 正确修改：
     * public synchronized void add()
     * public synchronized void compare()
     * */
    public void add() {
        log.info("add start");
        for (int i = 0; i < 10000; i++) {
            a++;
            b++;
        }
        log.info("add done");
    }

    public void compare() {
        log.info("compare start");
        for (int i = 0; i < 10000; i++) {
            if (a < b) {
                log.info("a:{}, b:{}, {}", a, b, a > b);
            }
        }
        log.info("compare end");
    }
}
