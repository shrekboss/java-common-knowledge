package org.bytedancer.crayzer.common_dev_error.coding.httpinvoke.feignpermethodtimeout;

import org.bytedancer.crayzer.common_dev_error.common.Utils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class CommonMistakesApplicationDefault {

    public static void main(String[] args) {
        Utils.loadPropertySource(CommonMistakesApplicationDefault.class, "default.properties");
        SpringApplication.run(CommonMistakesApplicationDefault.class, args);
    }
}

