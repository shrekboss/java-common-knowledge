package org.bytedancer.crayzer.design_mode_thingking.inaction.virtualwallet.service;

import org.bytedancer.crayzer.design_mode_thingking.inaction.virtualwallet.domain.VirtualWallet;
import org.bytedancer.crayzer.design_mode_thingking.inaction.virtualwallet.entity.VirtualWalletEntity;
import org.bytedancer.crayzer.design_mode_thingking.inaction.virtualwallet.repository.VirtualWalletRepository;
import org.bytedancer.crayzer.design_mode_thingking.inaction.virtualwallet.repository.VirtualWalletTransactionRepository;

import java.math.BigDecimal;

public class VirtualWalletService {
    // 通过构造函数或者IOC框架注入
    private VirtualWalletRepository walletRepo;
    private VirtualWalletTransactionRepository transactionRepo;

    public VirtualWallet getVirtualWallet(Long walletId) {
        VirtualWalletEntity walletEntity = walletRepo.getWalletEntity(walletId);
        VirtualWallet wallet = convert(walletEntity);
        return wallet;
    }

    public BigDecimal getBalance(Long walletId) {
        return walletRepo.getBalance(walletId);
    }

    public void debit(Long walletId, BigDecimal amount) {
        VirtualWalletEntity walletEntity = walletRepo.getWalletEntity(walletId);
        VirtualWallet wallet = convert(walletEntity);
        wallet.debit(amount);
        walletRepo.updateBalance(walletId, wallet.balance());
    }

    public void credit(Long walletId, BigDecimal amount) {
        VirtualWalletEntity walletEntity = walletRepo.getWalletEntity(walletId);
        VirtualWallet wallet = convert(walletEntity);
        wallet.credit(amount);
        walletRepo.updateBalance(walletId, wallet.balance());
    }

    public void transfer(Long fromWalletId, Long toWalletId, BigDecimal amount) {
        //...跟基于贫血模型的传统开发模式的代码一样...
    }

    private VirtualWallet convert(VirtualWalletEntity walletEntity) {
        return null;
    }
}
