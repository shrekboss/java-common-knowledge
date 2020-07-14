package org.bytedancer.crayzer.design_mode_pattern.behavioural.strategy.refactor.sample04.thirdPartyPayment;

import org.bytedancer.crayzer.design_mode_pattern.behavioural.strategy.refactor.sample04.PayState;

public interface Payment {
    PayState pay(String uid, double amount);
}
