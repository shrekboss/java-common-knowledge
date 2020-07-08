package org.bytedancer.crayzer.design_mode_thingking.inaction.virtualwallet.repository;

import org.bytedancer.crayzer.design_mode_thingking.inaction.virtualwallet.Status;
import org.bytedancer.crayzer.design_mode_thingking.inaction.virtualwallet.entity.VirtualWalletTransactionEntity;

public class VirtualWalletTransactionRepository {
    public void updateStatus(Long transactionId, Status closed) {
    }

    public Long saveTransaction(VirtualWalletTransactionEntity transactionEntity) {
        return null;
    }
}
