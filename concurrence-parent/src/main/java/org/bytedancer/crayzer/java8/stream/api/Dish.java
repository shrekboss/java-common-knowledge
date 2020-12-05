package org.bytedancer.crayzer.java8.stream.api;

import lombok.Data;

import java.lang.reflect.Type;

/**
 * @author yizhe.chen
 */
@Data
public class Dish {

    private String name;
    private boolean vegetarian;
    private int calories;

    private Type type;
}
