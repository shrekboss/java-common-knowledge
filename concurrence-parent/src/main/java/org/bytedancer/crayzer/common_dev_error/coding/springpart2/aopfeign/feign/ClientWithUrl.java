package org.bytedancer.crayzer.common_dev_error.coding.springpart2.aopfeign.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "anotherClient", url = "http://localhost:45678")
public interface ClientWithUrl {
    @GetMapping("/feignaop/server")
    String api();
}
