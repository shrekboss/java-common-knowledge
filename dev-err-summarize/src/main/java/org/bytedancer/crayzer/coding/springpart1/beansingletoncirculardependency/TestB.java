package org.bytedancer.crayzer.coding.springpart1.beansingletoncirculardependency;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TestB {
    @Autowired
    @Getter
    private TestA testA;
}