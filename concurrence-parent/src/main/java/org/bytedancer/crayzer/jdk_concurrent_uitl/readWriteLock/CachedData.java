package org.bytedancer.crayzer.jdk_concurrent_uitl.readWriteLock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class CachedData {
    private Object data;
    private volatile boolean cacheValid;
    private final ReadWriteLock rwl = new ReentrantReadWriteLock();

    private final Lock r = rwl.readLock();
    private final Lock w = rwl.writeLock();

    public void processCacheData() {
        r.lock();
        if (!cacheValid) {
            // 释放读锁，因为不允许读锁的升级
            r.unlock();
            // 获取写锁
            w.lock();
            try {
                if (!cacheValid) {
                    // data == ..... todo
                    cacheValid = true;
                }
                // 释放写锁前，降级读锁
                // 降级是可以的
                r.lock();
            } finally {
                w.unlock();
            }
        }
        // 此处仍然持有读锁
        try {
            use(data);
        } finally {
            r.unlock();
        }
    }

    private void use(Object data) {
        // to do biz@!
    }
}
