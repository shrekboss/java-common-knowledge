package org.bytedancer.crayzer.devmisuse.coding.springpart2.aopfeign;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "org.bytedancer.crayzer.devmisuse.coding.springpart2.aopfeign.feign")
public class Config {
}