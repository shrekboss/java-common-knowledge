package org.bytedancer.crayzer.design_mode_principle.ocp.extension.vo;

import lombok.Data;

@Data
public class ApiStatInfo {//省略constructor/getter/setter方法
    private String api;
    private long requestCount;
    private long errorCount;
    private long durationOfSeconds;
    private long timeoutCount;
}
