package org.bytedancer.crayzer.design_mode_refactor.test.refactor;

import org.bytedancer.crayzer.design_mode_refactor.test.original.WalletRpcService;

public class MockWalletRpcServiceTwo extends WalletRpcService {
    public String moveMoney(Long id, Long fromUserId, Long toUserId, Double amount) {
        return null;
    }
}