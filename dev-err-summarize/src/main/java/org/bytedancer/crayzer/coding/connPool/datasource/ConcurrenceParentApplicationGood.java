package org.bytedancer.crayzer.coding.connPool.datasource;

import org.bytedancer.crayzer.common.Utils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ConcurrenceParentApplicationGood {

    public static void main(String[] args) {
    	/* connPool#datasource */
		Utils.loadPropertySource(ConcurrenceParentApplicationGood.class, "good.properties");
    	SpringApplication.run(ConcurrenceParentApplicationGood.class, args);
    }

}
