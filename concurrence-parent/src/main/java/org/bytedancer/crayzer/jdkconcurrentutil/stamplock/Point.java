package org.bytedancer.crayzer.jdkconcurrentutil.stamplock;


import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.StampedLock;

/**
 * StampedLock 乐观读
 * {@link StampedLock}
 */
class Point {
    private int x, y;
    private final StampedLock sl =
            new StampedLock();
    //计算到原点的距离
    double distanceFromOrigin(int x, int y) {
        // 乐观读
        long stamp = sl.tryOptimisticRead();
        // 读入局部变量，读的过程数据可能被修改
        int curX = x, curY = y;
        //判断执行读操作期间，是否存在写操作，如果存在，则sl.validate返回false
        if (!sl.validate(stamp)){
            // 升级为悲观读锁
            stamp = sl.readLock();
            try {
                curX = x;
                curY = y;
            } finally {
                //释放悲观读锁
                sl.unlockRead(stamp);
            }
        }
        return Math.sqrt(curX * curX + curY * curY);
    }

    public static void main(String[] args) {
        Point point = new Point();
        Executor executor = Executors.newFixedThreadPool(2);
//        CountDownLatch latch = new CountDownLatch(2);
        final CyclicBarrier barrier = new CyclicBarrier(2);
        executor.execute(()-> {
            double v = point.distanceFromOrigin(3, 4);
            System.out.println(v);
            try {
                barrier.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
        });
        executor.execute(()-> {
            double v = point.distanceFromOrigin(6, 8);
            System.out.println(v);
            try {
                barrier.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
        });
        // 等待两个查询操作结束
//        latch.await();
    }
}
