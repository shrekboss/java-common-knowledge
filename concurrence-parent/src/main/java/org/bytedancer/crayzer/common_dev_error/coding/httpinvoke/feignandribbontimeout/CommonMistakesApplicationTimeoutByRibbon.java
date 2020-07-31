package org.bytedancer.crayzer.common_dev_error.coding.httpinvoke.feignandribbontimeout;

import org.bytedancer.crayzer.common_dev_error.coding.common.Utils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CommonMistakesApplicationTimeoutByRibbon {

    public static void main(String[] args) {
        Utils.loadPropertySource(FeignAndRibbonController.class, "default.properties");
        Utils.loadPropertySource(FeignAndRibbonController.class, "ribbon.properties");
        SpringApplication.run(CommonMistakesApplicationTimeoutByRibbon.class, args);
    }
}

