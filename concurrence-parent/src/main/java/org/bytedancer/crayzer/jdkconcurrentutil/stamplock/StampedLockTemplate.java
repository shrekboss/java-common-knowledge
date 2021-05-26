package org.bytedancer.crayzer.jdkconcurrentutil.stamplock;

import java.util.concurrent.locks.StampedLock;

/**
 * {@link StampedLock} Java 官方模板
 */
public class StampedLockTemplate {

    public void methodForRead() {
        final StampedLock sl = new StampedLock();

        // 乐观读
        long stamp = sl.tryOptimisticRead();
        // 读入方法局部变量
        // ......
        // 校验stamp
        if (!sl.validate(stamp)) {
            // 升级为悲观读锁
            stamp = sl.readLock();
            try {
                // 读入方法局部变量
                // .....
            } finally {
                //释放悲观读锁
                sl.unlockRead(stamp);
            }
        }
        // 使用方法局部变量执行业务操作
        // ......
    }
}

