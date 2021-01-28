package org.bytedancer.crayzer.jdkconcurrentutil.semaphore.reconciliationApp;

public class ReconciliationSysV1 {

    private POrder pOrder;
    private DOrder dOrder;

    public void reconcilation() {

        while (existReconciliationOrder()) {
            Thread t1 = new Thread(() -> {
                pOrder = getPOrders();
            });
            t1.start();

            Thread t2 = new Thread(() -> {
                dOrder = getDOrders();
            });
            t2.start();
            try {
                t1.join();
                t2.join();
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
