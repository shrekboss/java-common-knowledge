package org.bytedancer.crayzer.jdkconcurrentutil.readwritelock;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ConcurrentCache<K, V> {
    private final Map<K, V> m = new HashMap<>();
    private final ReadWriteLock rwl = new ReentrantReadWriteLock();
    // 读锁
    private final Lock r = rwl.readLock();
    // 写锁
    private final Lock w = rwl.writeLock();

    // 读缓存
    V get(K key) {
        V v = null;
        r.lock();
        try {
            v = m.get(key);
        } finally {
            r.unlock();
        }
        if (v != null) return v;

        w.lock();
        try {
            v = m.get(key);
            if (v == null) {
                //查询数据库
                //v=省略代码无数
                m.put(key, v);
            }
        } finally {
            w.unlock();
        }
        return v;
    }

    // 写缓存
    V put(K key, V value) {
        w.lock();
        try {
            return m.put(key, value);
        } finally {
            w.unlock();
        }
    }
}
