package org.bytedancer.crayzer.design.apidesgin.apiresposne;

import lombok.Data;

@Data
public class APIResponse<T> {
    private boolean success;
    private T data;
    private int code;
    private String message;
}
