package org.bytedancer.crayzer.design_mode_pattern.creational.factory.example.productFactory.abstractFactory;

public class TCLFactory implements EFactory {
    public Television produceTelevision() {
        return new TCLTelevision();
    }

    public AirConditioner produceAirConditioner() {
        return new TCLAirConditioner();
    }
}