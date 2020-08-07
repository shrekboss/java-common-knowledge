package org.bytedancer.crayzer.devmisuse.coding.connPool.datasource;

import org.bytedancer.crayzer.devmisuse.common.Utils;
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
