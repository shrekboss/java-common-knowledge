package org.bytedancer.crayzer.devmisuse.safety.clientdata.trustclientcalculation;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Item {
    private long itemId;
    private BigDecimal itemPrice;
}
