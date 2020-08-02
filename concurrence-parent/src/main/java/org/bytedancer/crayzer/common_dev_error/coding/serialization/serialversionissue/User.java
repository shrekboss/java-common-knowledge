package org.bytedancer.crayzer.common_dev_error.coding.serialization.serialversionissue;

import lombok.Data;

import java.io.Serializable;

@Data
public class User implements Serializable {
    private static final long serialVersionUID = 3768219943056108047L;
    private String name;
    private int age;
}