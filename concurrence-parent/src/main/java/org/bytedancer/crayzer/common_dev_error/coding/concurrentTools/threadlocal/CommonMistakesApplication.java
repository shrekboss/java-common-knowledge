package org.bytedancer.crayzer.common_dev_error.coding.concurrentTools.threadlocal;

import org.bytedancer.crayzer.common_dev_error.common.Utils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class CommonMistakesApplication {

    public static void main(String[] args) {
        Utils.loadPropertySource(CommonMistakesApplication.class, "tomcat.properties");

        SpringApplication.run(CommonMistakesApplication.class, args);
    }
}

