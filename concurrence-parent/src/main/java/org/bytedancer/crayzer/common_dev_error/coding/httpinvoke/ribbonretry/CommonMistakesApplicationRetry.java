package org.bytedancer.crayzer.common_dev_error.coding.httpinvoke.ribbonretry;

import org.bytedancer.crayzer.common_dev_error.common.Utils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class CommonMistakesApplicationRetry {

    public static void main(String[] args) {
        Utils.loadPropertySource(
                CommonMistakesApplicationRetry.class,
                "default-ribbon.properties");
        SpringApplication.run(CommonMistakesApplicationRetry.class, args);
    }
}

