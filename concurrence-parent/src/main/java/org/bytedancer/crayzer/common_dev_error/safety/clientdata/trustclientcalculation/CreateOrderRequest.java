package org.bytedancer.crayzer.common_dev_error.safety.clientdata.trustclientcalculation;

import lombok.Data;

@Data
public class CreateOrderRequest {
    private long itemId;
    private int quantity;
}
