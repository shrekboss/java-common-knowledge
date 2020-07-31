package org.bytedancer.crayzer.common_dev_error.coding.httpinvoke.ribbonretry;

import org.bytedancer.crayzer.common_dev_error.coding.common.Utils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class CommonMistakesApplicationNoRetry {

    public static void main(String[] args) {

        Utils.loadPropertySource(CommonMistakesApplicationNoRetry.class,"noretry-ribbon.properties");
        SpringApplication.run(CommonMistakesApplicationNoRetry.class, args);
    }
}
