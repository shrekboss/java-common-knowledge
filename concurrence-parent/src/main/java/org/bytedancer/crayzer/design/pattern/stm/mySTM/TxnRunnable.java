package org.bytedancer.crayzer.design.pattern.stm.mySTM;

@FunctionalInterface
public interface TxnRunnable {
    void run(Txn txn);
}
