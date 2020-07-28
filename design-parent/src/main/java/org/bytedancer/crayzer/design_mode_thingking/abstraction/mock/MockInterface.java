package org.bytedancer.crayzer.design_mode_thingking.abstraction.mock;

public class MockInterface {
    protected MockInterface() {

    }

    public void method(){
        throw new RuntimeException();
    }
}
