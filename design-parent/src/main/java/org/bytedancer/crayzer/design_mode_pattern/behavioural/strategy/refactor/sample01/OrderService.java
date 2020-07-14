package org.bytedancer.crayzer.design_mode_pattern.behavioural.strategy.refactor.sample01;

import org.bytedancer.crayzer.design_mode_pattern.behavioural.strategy.original.sample01.Order;
import org.bytedancer.crayzer.design_mode_pattern.behavioural.strategy.original.sample01.OrderType;

// 策略的使用
public class OrderService {
    public double discount(Order order) {
        OrderType type = order.getType();
        DiscountStrategy discountStrategy = DiscountStrategyFactory1.getDiscountStrategy(type);
        return discountStrategy.calDiscount(order);
    }
}
