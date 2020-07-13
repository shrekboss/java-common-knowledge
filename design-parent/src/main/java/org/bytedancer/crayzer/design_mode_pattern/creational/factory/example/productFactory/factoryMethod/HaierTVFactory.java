package org.bytedancer.crayzer.design_mode_pattern.creational.factory.example.productFactory.factoryMethod;

import org.crayzer.demo.factory.HaierTV;
import org.crayzer.demo.factory.TV;

public class HaierTVFactory implements TVFactory {
    @Override
    public TV produceTV() {
        System.out.println("海尔电视机工厂生产海尔电视机。");
        return new HaierTV();
    }
}
