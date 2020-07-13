package org.bytedancer.crayzer.design_mode_pattern.creational.factory.example.productFactory.abstractFactory;

public interface EFactory {
    Television produceTelevision();

    AirConditioner produceAirConditioner();
}