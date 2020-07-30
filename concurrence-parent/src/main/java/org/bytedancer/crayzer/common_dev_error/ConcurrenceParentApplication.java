package org.bytedancer.crayzer.common_dev_error;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ConcurrenceParentApplication {

    public static void main(String[] args) {
    	/* connPool#datasource */
		// Utils.loadPropertySource(ConcurrenceParentApplication.class, "connPool/datasource/bad.properties");
		// Utils.loadPropertySource(ConcurrenceParentApplication.class, "connPool/datasource/good.properties");

    	SpringApplication.run(ConcurrenceParentApplication.class, args);
    }

}
