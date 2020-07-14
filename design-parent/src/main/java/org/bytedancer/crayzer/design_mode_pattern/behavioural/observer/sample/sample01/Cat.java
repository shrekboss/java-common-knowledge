package org.bytedancer.crayzer.design_mode_pattern.behavioural.observer.sample.sample01;

public class Cat extends MySubject{
    @Override
    public void cry() {
        System.out.println("猫叫！");
        System.out.println("----------------------------");

        for (MyObserver observer : observers) {
            observer.response();
        }
    }
}
