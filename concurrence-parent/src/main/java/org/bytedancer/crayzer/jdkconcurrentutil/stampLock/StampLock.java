package org.bytedancer.crayzer.jdkconcurrentutil.stampLock;

import java.util.concurrent.locks.StampedLock;

/**
 * @author yizhe.chen
 */
public class StampLock {
    public static void main(String[] args) {

        final StampedLock sl = new StampedLock();

        // 获取/释放悲观读锁示意代码
        long stamp = sl.readLock();
        try {
            //省略业务相关代码
        } finally {
            sl.unlockRead(stamp);
        }

        // 获取/释放写锁示意代码
        long stamp1 = sl.writeLock();
        try {
            //省略业务相关代码
        } finally {
            sl.unlockWrite(stamp1);
        }
    }
}
