package org.bytedancer.crayzer.design.apidesgin.apiresposne;

import org.bytedancer.crayzer.common.Utils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CommonMistakesApplication {

    public static void main(String[] args) {
        Utils.loadPropertySource(CommonMistakesApplication.class, "config.properties");
        SpringApplication.run(CommonMistakesApplication.class, args);
    }
}

