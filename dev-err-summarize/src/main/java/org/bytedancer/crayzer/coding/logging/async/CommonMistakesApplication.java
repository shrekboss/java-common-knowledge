package org.bytedancer.crayzer.coding.logging.async;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CommonMistakesApplication {

    public static void main(String[] args) {
        // 同步 Appender /logging/performance
        // System.setProperty("logging.config", "classpath:org/bytedancer/crayzer/common_dev_error/coding/logging/async/performance_sync.xml");
        // 异步 Appender /logging/performance
        // System.setProperty("logging.config", "classpath:org/bytedancer/crayzer/common_dev_error/coding/logging/async/performance_async.xml");

        // /logging/manylog
        System.setProperty("logging.config", "classpath:org/bytedancer/crayzer/common_dev_error/coding/logging/async/asyncwrong.xml");

        SpringApplication.run(CommonMistakesApplication.class, args);
    }
}

