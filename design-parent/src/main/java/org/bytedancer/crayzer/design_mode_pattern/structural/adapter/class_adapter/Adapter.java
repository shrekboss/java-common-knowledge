package org.bytedancer.crayzer.design_mode_pattern.structural.adapter.class_adapter;

import org.bytedancer.crayzer.design_mode_pattern.structural.adapter.Source;
import org.bytedancer.crayzer.design_mode_pattern.structural.adapter.Targetable;

public class Adapter extends Source implements Targetable {

    @Override
    public void method2() {
        System.out.println("this is the targetable method!");
    }
}
