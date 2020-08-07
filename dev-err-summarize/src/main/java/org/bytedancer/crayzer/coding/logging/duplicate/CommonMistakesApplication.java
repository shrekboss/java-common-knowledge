package org.bytedancer.crayzer.coding.logging.duplicate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CommonMistakesApplication {

    public static void main(String[] args) {
        /* 日志重复问题 */
        System.setProperty("logging.config", "classpath:org/bytedancer/crayzer/common_dev_error/coding/logging/duplicate/loggerwrong.xml");
        // System.setProperty("logging.config", "classpath:org/bytedancer/crayzer/common_dev_error/coding/logging/duplicate/loggerright1.xml");
        // System.setProperty("logging.config", "classpath:org/bytedancer/crayzer/common_dev_error/coding/logging/duplicate/loggerright2.xml");

        /* 错误配置 LevelFilter 造成日志重复记录 */
        // System.setProperty("logging.config", "classpath:org/bytedancer/crayzer/common_dev_error/coding/logging/duplicate/filterwrong.xml");
        // System.setProperty("logging.config", "classpath:org/bytedancer/crayzer/common_dev_error/coding/logging/duplicate/filterright.xml");

        /* 自定义 filter */
        // System.setProperty("logging.config", "classpath:org/bytedancer/crayzer/common_dev_error/coding/logging/duplicate/multiplelevelsfilter.xml");
        SpringApplication.run(CommonMistakesApplication.class, args);
    }
}

