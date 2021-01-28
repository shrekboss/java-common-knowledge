package org.bytedancer.crayzer.jdkconcurrentuitl.condition;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class PrintStr implements Runnable {
    private static final int COUNT = 10;
    private final ReentrantLock reentrantLock;
    private final Condition thisCondition;
    private final Condition nextCondition;
    private final String printContext;

    public PrintStr(ReentrantLock reentrantLock, Condition thisCondition,
                    Condition nextCondition, String printContext) {
        this.reentrantLock = new ReentrantLock();
        this.thisCondition = thisCondition;
        this.nextCondition = nextCondition;
        this.printContext = printContext;
    }

    @Override
    public void run() {
        reentrantLock.lock();
        try {
            for (int i = 0; i < COUNT; i++) {
                System.out.print(printContext);
                nextCondition.signal();
                if (i < COUNT - 1) {
                    try {
                        thisCondition.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        } finally {
            reentrantLock.unlock();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ReentrantLock lock = new ReentrantLock();
        Condition conditionX = lock.newCondition();
        Condition conditionY = lock.newCondition();
        Condition conditionZ = lock.newCondition();
        Runnable target;
        Thread printX = new Thread(new PrintStr(lock, conditionX, conditionY, "X"));
        Thread printY = new Thread(new PrintStr(lock, conditionY, conditionZ, "Y"));
        Thread printZ = new Thread(new PrintStr(lock, conditionZ, conditionX, "Z"));

        printX.start();
        Thread.sleep(100);
        printY.start();
        Thread.sleep(100);
        printZ.start();
        Thread.sleep(100);
    }
}
