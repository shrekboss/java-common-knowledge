package org.bytedancer.crayzer.arithmetic.sleepsort;

/**
 * @author yizhe.chen
 */
public class SleepSort {

    public static void main(String[] args) {
        int[] ints = {1, 4, 7, 3, 8, 9, 2, 6, 5};
        SortThread[] sortThreads = new SortThread[ints.length];

        for (int i = 0; i < sortThreads.length; i++) {
            sortThreads[i] = new SortThread(ints[i]);
        }

        for (SortThread sortThread : sortThreads) {
            sortThread.start();
        }
    }
}

class SortThread extends Thread {
    int ms;

    public SortThread(int ms) {
        this.ms = ms;
    }

    @Override
    public void run() {
        try {
            sleep(ms * 10L + 10);
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
        System.out.println(ms);
    }
}
