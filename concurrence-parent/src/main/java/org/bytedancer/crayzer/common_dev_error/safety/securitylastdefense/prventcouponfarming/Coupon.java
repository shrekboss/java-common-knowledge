package org.bytedancer.crayzer.common_dev_error.safety.securitylastdefense.prventcouponfarming;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class Coupon {
    private long userId;
    private BigDecimal amount;
}
