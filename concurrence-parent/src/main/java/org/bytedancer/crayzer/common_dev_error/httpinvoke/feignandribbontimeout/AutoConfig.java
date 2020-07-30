package org.bytedancer.crayzer.common_dev_error.httpinvoke.feignandribbontimeout;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "org.bytedancer.crayzer.common_dev_error.httpinvoke.feignandribbontimeout")
public class AutoConfig {
}
