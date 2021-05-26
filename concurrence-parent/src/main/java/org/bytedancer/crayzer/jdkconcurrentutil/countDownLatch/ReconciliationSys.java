package org.bytedancer.crayzer.jdkconcurrentutil.countDownLatch;

import org.bytedancer.crayzer.jdkconcurrentutil.DOrder;
import org.bytedancer.crayzer.jdkconcurrentutil.POrder;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ReconciliationSys {

    private POrder pOrder;
    private DOrder dOrder;

    public void unreconciled() throws InterruptedException {

        // 创建2个线程的线程池
        Executor executor =
                Executors.newFixedThreadPool(2);
        while(existUnreconciledOrder()){
            // 计数器初始化为2
            CountDownLatch latch =
                    new CountDownLatch(2);
            // 查询未对账订单
            executor.execute(()-> {
                pOrder = getPOrders();
                latch.countDown();
            });
            // 查询派送单
            executor.execute(()-> {
                dOrder = getDOrders();
                latch.countDown();
            });

            // 等待两个查询操作结束
            latch.await();

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
