package org.bytedancer.crayzer.common_dev_error.design.apidesgin.apiresposne;

import lombok.Data;

@Data
public class APIResponse<T> {
    private boolean success;
    private T data;
    private int code;
    private String message;
}
