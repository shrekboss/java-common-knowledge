package org.bytedancer.crayzer.devmisuse.coding.httpinvoke.ribbonretry;

import org.bytedancer.crayzer.devmisuse.common.Utils;
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

