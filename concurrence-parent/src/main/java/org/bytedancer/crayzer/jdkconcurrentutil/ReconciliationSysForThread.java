package org.bytedancer.crayzer.jdkconcurrentutil;

public class ReconciliationSysForThread {

    private POrder pOrder;
    private DOrder dOrder;

    public void unreconciled() {

        // 存在未对账订单
        while (existUnreconciledOrder()) {
            // 查询派送单
            Thread t1 = new Thread(() -> pOrder = getPOrders());
            t1.start();
            // 查询派送单
            Thread t2 = new Thread(() -> dOrder = getDOrders());
            t2.start();
            try {
                t1.join();
                t2.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // 执行对账操作
            Object object = check(pOrder, dOrder);
            // 差异写入差异库
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

    private boolean existUnreconciledOrder() {
        return true;
    }
}
