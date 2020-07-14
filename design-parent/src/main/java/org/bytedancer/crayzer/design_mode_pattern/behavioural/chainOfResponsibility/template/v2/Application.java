package org.bytedancer.crayzer.design_mode_pattern.behavioural.chainOfResponsibility.template.v2;

// 使用举例
public class Application {
    public static void main(String[] args) {
        HandlerChain chain = new HandlerChain();
        chain.addHandler(new HandlerA());
        chain.addHandler(new HandlerB());
        chain.handle();
    }
}
