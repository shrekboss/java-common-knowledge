package org.bytedancer.crayzer.design_mode_pattern.structural.decorator.rebot;

public class Robot extends Changer {
    public Robot(Transform transform) {
        super(transform);
        System.out.println("变成机器人！");
    }

    public void say() {
        System.out.println("说话！");
    }
}
