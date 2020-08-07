package org.bytedancer.crayzer.devmisuse.coding.httpinvoke.ribbonretry;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;


@Configuration
@EnableFeignClients(basePackages = "org.bytedancer.crayzer.devmisuse.coding.httpinvoke.ribbonretry.feign")
public class AutoConfig {
}
