package org.bytedancer.crayzer.coding.concurrentTools.multiasynctasks;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author yizhe.chen
 */
@Slf4j
public class CommonMistakesApplication {

    private static ExecutorService threadPool = Executors.newFixedThreadPool(10);

    public static void main(String[] args) {
        // result [-1, -1, -1, -1] took 116 ms
        // test1();

        // result [1, 2, 3] took 3100 ms
        test2();
    }

    private static void test1() {
        long begin = System.currentTimeMillis();
        List<Future<Integer>> futures = IntStream.rangeClosed(1, 4)
                .mapToObj(i -> threadPool.submit(getAsyncTask(i)))
                .collect(Collectors.toList());
        List<Integer> result = futures.stream().map(future -> {
            try {
                return future.get(2, TimeUnit.MILLISECONDS);
            } catch (Exception e) {
                e.printStackTrace();
                return -1;
            }
        }).collect(Collectors.toList());
        log.info("result {} took {} ms", result, System.currentTimeMillis() - begin);
    }

    private static Callable<Integer> getAsyncTask(int i) {
        return () -> {
            TimeUnit.SECONDS.sleep(i);
            return i;
        };
    }

    private static void test2() {
        long begin = System.currentTimeMillis();
        int count = 4;
        List<Integer> result = new ArrayList<>(count);
        CountDownLatch countDownLatch = new CountDownLatch(count);
        IntStream.rangeClosed(1, count).forEach(i -> threadPool.execute(executeAsyncTask(i, countDownLatch, result)));
        try {
            countDownLatch.await(3, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("result {} took {} ms", result, System.currentTimeMillis() - begin);
    }

    private static Runnable executeAsyncTask(int i, CountDownLatch countDownLatch, List<Integer> result) {
        return () -> {
            try {
                TimeUnit.SECONDS.sleep(i);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (result) {
                result.add(i);
            }
            countDownLatch.countDown();
        };
    }
}

