package org.bytedancer.crayzer.common.dev.err.threadsafe.lock;


public class WrongData {
    private static int counter = 0;

    public static int reset() {
        counter = 0;
        return counter;
    }

    public synchronized void wrong() {
        counter++;
    }

    public static int getCounter() {
        return counter;
    }
}
