package org.bytedancer.crayzer.jdk_concurrent_uitl.semaphore;

import java.util.List;
import java.util.Vector;
import java.util.concurrent.Semaphore;
import java.util.function.Function;

public class ObjPool<T, R> {
    private final List<T> pool;
    private final Semaphore sem;

    public ObjPool(int size, T t) {
        /*
         * 需要用线程安全的vector，因为信号量支持多个线程进入临界区，
         * 执行list的add和remove方法时可能是多线程并发执行 */
        pool = new Vector<T>() {};
        for (int i = 0; i < size; i++) {
            pool.add(t);
        }
        sem = new Semaphore(size);
    }

    public R exec(Function<T, R> func) {
        T t = null;
        try {
            sem.acquire();
            t = pool.remove(0);
            return func.apply(t);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            pool.add(t);
            sem.release();
        }
        return null;
    }

    public static void main(String[] args) {
        /* 假设对象池的大小是 10，信号量的计数器初始化为 10，那么前 10 个线程调用 acquire() 方法，
        都能继续执行，相当于通过了信号灯，而其他线程则会阻塞在 acquire() 方法上。*/
        ObjPool<Integer, String> pool = new ObjPool<>(10, 2);
        for (int i = 0; i < 20; i++) {
            pool.exec(t -> {
                System.out.println(t + System.currentTimeMillis());
                return t.toString();
            });
        }
    }
}
