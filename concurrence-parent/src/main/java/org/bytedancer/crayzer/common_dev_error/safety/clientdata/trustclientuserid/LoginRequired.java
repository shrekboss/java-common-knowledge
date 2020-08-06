package org.bytedancer.crayzer.common_dev_error.safety.clientdata.trustclientuserid;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
@Documented
public @interface LoginRequired {
    String sessionKey() default "currentUser";
}