package org.bytedancer.crayzer.java7.autoclose;

/**
 * @author yizhe.chen
 */
public class Resource2 implements AutoCloseable {

    public void sayHello() {
        System.out.println("Resource2 say hello");
    }

    @Override
    public void close() throws Exception {
        System.out.println("Resource2 is closed");
    }
}
