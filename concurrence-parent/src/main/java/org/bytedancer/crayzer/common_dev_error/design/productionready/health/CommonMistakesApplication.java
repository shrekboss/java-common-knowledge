package org.bytedancer.crayzer.common_dev_error.design.productionready.health;

import org.bytedancer.crayzer.common_dev_error.common.Utils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class CommonMistakesApplication {

    public static void main(String[] args) {
        Utils.loadPropertySource(CommonMistakesApplication.class, "actuator.properties");

        SpringApplication.run(CommonMistakesApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}

