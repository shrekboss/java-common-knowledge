package org.bytedancer.crayzer.common_dev_error.design.apidesgin.apiresposne;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderInfo {
    private String status;
    private long orderId;
}
