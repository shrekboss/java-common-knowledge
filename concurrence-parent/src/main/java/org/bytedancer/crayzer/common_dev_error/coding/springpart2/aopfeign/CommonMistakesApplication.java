package org.bytedancer.crayzer.common_dev_error.coding.springpart2.aopfeign;

import org.bytedancer.crayzer.common_dev_error.common.Utils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CommonMistakesApplication {

    public static void main(String[] args) {
        Utils.loadPropertySource(CommonMistakesApplication.class, "feign.properties");
        SpringApplication.run(CommonMistakesApplication.class, args);
    }
}

