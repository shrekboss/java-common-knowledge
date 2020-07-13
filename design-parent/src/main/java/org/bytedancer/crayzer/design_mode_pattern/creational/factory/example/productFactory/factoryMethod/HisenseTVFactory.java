package org.bytedancer.crayzer.design_mode_pattern.creational.factory.example.productFactory.factoryMethod;

import org.bytedancer.crayzer.design_mode_pattern.creational.factory.example.productFactory.HisenseTV;
import org.bytedancer.crayzer.design_mode_pattern.creational.factory.example.productFactory.TV;

public class HisenseTVFactory implements TVFactory {
    @Override
    public TV produceTV() {
        System.out.println("海信电视机工厂生产海信电视机。");
        return new HisenseTV();
    }
}
