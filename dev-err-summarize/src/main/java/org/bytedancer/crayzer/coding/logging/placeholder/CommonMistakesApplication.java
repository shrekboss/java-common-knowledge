package org.bytedancer.crayzer.coding.logging.placeholder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CommonMistakesApplication {

    public static void main(String[] args) {
        System.setProperty("logging.config", "classpath:org/bytedancer/crayzer/common_dev_error/coding/logging/placeholder/logger_debug.xml");

        SpringApplication.run(CommonMistakesApplication.class, args);
    }
}

