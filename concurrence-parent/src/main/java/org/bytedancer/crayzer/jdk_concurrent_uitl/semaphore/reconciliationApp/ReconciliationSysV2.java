package org.bytedancer.crayzer.jdk_concurrent_uitl.semaphore.reconciliationApp;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ReconciliationSysV2 {

    private POrder pOrder;
    private DOrder dOrder;

    public void reconcilation() {
        Executor executor = Executors.newFixedThreadPool(2);

        while (existReconciliationOrder()) {
            CountDownLatch latch = new CountDownLatch(2);

            executor.execute(() -> {
                pOrder = getPOrders();
                latch.countDown();
            });
            executor.execute(() -> {
                dOrder = getDOrders();
                latch.countDown();
            });

            /* ？？如何实现等待？？*/
            // 等待两个查询操作结束
            try {
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Object object = check(pOrder, dOrder);
            save(object);
        }
    }

    private POrder getPOrders() {
        return null;
    }

    private DOrder getDOrders() {
        return null;
    }

    private Object check(POrder pOrder, DOrder dOrder) {
        return null;
    }

    private void save(Object object) {

    }

    private boolean existReconciliationOrder() {
        return true;
    }

}
