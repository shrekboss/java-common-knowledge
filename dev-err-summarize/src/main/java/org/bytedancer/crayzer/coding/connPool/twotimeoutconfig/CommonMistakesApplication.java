package org.bytedancer.crayzer.coding.connPool.twotimeoutconfig;

import org.bytedancer.crayzer.common.Utils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CommonMistakesApplication {

    public static void main(String[] args) {
        Utils.loadPropertySource(CommonMistakesApplication.class, "hikari.properties");

        SpringApplication.run(CommonMistakesApplication.class, args);
    }

}

