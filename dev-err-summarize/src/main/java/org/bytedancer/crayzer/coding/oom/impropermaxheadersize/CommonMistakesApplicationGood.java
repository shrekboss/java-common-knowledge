package org.bytedancer.crayzer.coding.oom.impropermaxheadersize;

import lombok.extern.slf4j.Slf4j;
import org.bytedancer.crayzer.common.Utils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class CommonMistakesApplicationGood {
    public static void main(String[] args) {
        Utils.loadPropertySource(CommonMistakesApplicationGood.class, "good.properties");
        SpringApplication.run(CommonMistakesApplicationGood.class, args);
    }

}
