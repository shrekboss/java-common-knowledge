package org.bytedancer.crayzer.common_dev_error.coding.springpart1.beansingletonandorder;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class TestC {
    @Getter
    private TestD testD;

    @Autowired
    public TestC(@Lazy TestD testD) {
        this.testD = testD;
    }
}