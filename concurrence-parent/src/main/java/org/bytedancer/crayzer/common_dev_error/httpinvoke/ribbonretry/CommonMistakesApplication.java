package org.bytedancer.crayzer.common_dev_error.httpinvoke.ribbonretry;

import org.bytedancer.crayzer.common_dev_error.common.Utils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class CommonMistakesApplication {

    public static void main(String[] args) {
        Utils.loadPropertySource(
                CommonMistakesApplication.class,
                "default-ribbon.properties");
        SpringApplication.run(CommonMistakesApplication.class, args);
    }
}

