package org.bytedancer.crayzer.common_dev_error.coding.httpinvoke.feignandribbontimeout;

import org.bytedancer.crayzer.common_dev_error.common.Utils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class CommonMistakesApplicationDefault {

    public static void main(String[] args) {
        Utils.loadPropertySource(
                FeignAndRibbonController.class,
                "default.properties");
        SpringApplication.run(CommonMistakesApplicationDefault.class, args);
    }
}

