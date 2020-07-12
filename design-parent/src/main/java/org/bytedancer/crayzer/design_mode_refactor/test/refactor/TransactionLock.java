package org.bytedancer.crayzer.design_mode_refactor.test.refactor;

import org.bytedancer.crayzer.design_mode_refactor.test.RedisDistributedLock;

public class TransactionLock {

    public boolean lock(String id) {
        return RedisDistributedLock.getSingletonInstance().lockTransaction(id);
    }

    public void unlock(String id) {
        RedisDistributedLock.getSingletonInstance().unlockTransaction(id);
    }
}
