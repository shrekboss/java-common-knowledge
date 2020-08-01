package org.bytedancer.crayzer.common_dev_error.coding.logging.duplicate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CommonMistakesApplication {

    public static void main(String[] args) {
        /* 日志重复问题 */
        // System.setProperty("logging.config", "classpath:org/bytedancer/crayzer/common_dev_error/coding/logging/duplicate/loggerwrong.xml");
        // System.setProperty("logging.config", "classpath:org/bytedancer/crayzer/common_dev_error/coding/logging/duplicate/loggerright1.xml");
        // System.setProperty("logging.config", "classpath:org/bytedancer/crayzer/common_dev_error/coding/logging/duplicate/loggerright2.xml");

        /* 错误配置 LevelFilter 造成日志重复记录 */
        // System.setProperty("logging.config", "classpath:org/bytedancer/crayzer/common_dev_error/coding/logging/duplicate/filterwrong.xml");
        System.setProperty("logging.config", "classpath:org/bytedancer/crayzer/common_dev_error/coding/logging/duplicate/filterright.xml");

        // System.setProperty("logging.config", "classpath:org/geekbang/time/commonmistakes/logging/duplicate/multiplelevelsfilter.xml");
        // System.setProperty("logging.config", "classpath:org/geekbang/time/commonmistakes/logging/duplicate/multiplelevelsfilter.xml");
        // System.setProperty("logging.config", "classpath:org/geekbang/time/commonmistakes/logging/duplicate/multiplelevelsfilter.xml");
        SpringApplication.run(CommonMistakesApplication.class, args);
    }
}

