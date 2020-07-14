package org.bytedancer.crayzer.design_mode_pattern.behavioural.strategy.refactor.sample04.thirdPartyPayment;

import org.bytedancer.crayzer.design_mode_pattern.behavioural.strategy.refactor.sample04.PayState;

public class JDPay implements Payment{
    @Override
    public PayState pay(String uid, double amount) {
        System.out.println("欢迎使用京东支付");
        System.out.println("直接从京东白条扣款");
        return new PayState(200, uid + "支付成功", amount);
    }
}
