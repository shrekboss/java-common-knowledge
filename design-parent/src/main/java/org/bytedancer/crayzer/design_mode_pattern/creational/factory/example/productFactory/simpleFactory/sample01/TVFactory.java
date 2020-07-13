package org.bytedancer.crayzer.design_mode_pattern.creational.factory.example.productFactory.simpleFactory.sample01;

import org.crayzer.demo.factory.HaierTV;
import org.crayzer.demo.factory.HisenseTV;
import org.crayzer.demo.factory.TV;

public class TVFactory {
    public static TV produceTV(String brand) {
        if ("Hisense".equalsIgnoreCase(brand)) {
            return new HisenseTV();
        } else if ("Haier".equalsIgnoreCase(brand)) {
            return new HaierTV();
        } else {
            System.out.println("本工厂无法提高您所需的产品！");
            return null;
        }
    }
}
