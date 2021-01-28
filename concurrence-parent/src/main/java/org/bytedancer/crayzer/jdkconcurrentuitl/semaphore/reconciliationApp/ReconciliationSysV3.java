package org.bytedancer.crayzer.jdkconcurrentuitl.semaphore.reconciliationApp;

import java.util.Vector;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ReconciliationSysV3 {

    // 订单队列
    private Vector<POrder> pos;
    // 派送单队列
    private Vector<DOrder> dos;
    /**
     * 执行回调的线程池
     * 线程池大小是 1，只有 1 个线程，主要原因是 check() 方法的耗时比 getPOrders() 和 getDOrders() 都要短，
     * 所以没必要用多个线程，同时单线程能保证访问的数据不存在并发问题。
     */
    private final Executor executor = Executors.newFixedThreadPool(1);

    final CyclicBarrier barrier =
            new CyclicBarrier(2, () -> {
                /*
                 * 直接在回调函数里调用 check() 方法是否可以呢？绝对不可以。这个要分析一下回调函数和唤醒等待线程之间的关系
                 * 通过源码你会发现 CyclicBarrier 是同步调用回调函数之后才唤醒等待的线程，如果我们在回调函数里直接调用 check() 方法，
                 * 那就意味着在执行 check() 的时候，是不能同时执行 getPOrders() 和 getDOrders() 的，这样就起不到提升性能的作用。
                 * */
                executor.execute(() -> check());
            });

    public void check() {
        POrder p = pos.remove(0);
        DOrder d = dos.remove(0);
        // 执行对账操作
        Object diff = check(p, d);
        // 差异写入差异库
        save(diff);
    }

    /**
     * describe: 两个队列入的步调和出的步调是一致的, 从而保证双队列元素之间的一一对应关系
     **/
    void checkAll() {
        // 循环查询订单库
        Thread T1 = new Thread(() -> {
            while (existReconciliationOrder()) {
                // 查询订单库
                pos.add(getPOrders());
                // 等待
                try {
                    barrier.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }
        });
        T1.start();
        // 循环查询运单库
        Thread T2 = new Thread(() -> {
            while (existReconciliationOrder()) {
                // 查询运单库
                dos.add(getDOrders());
                // 等待
                try {
                    barrier.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }
        });
        T2.start();
    }

    private void save(Object diff) {
    }

    private Object check(POrder p, DOrder d) {
        return null;
    }

    private boolean existReconciliationOrder() {
        return true;
    }

    private POrder getPOrders() {
        return null;
    }

    private DOrder getDOrders() {
        return null;
    }
}
