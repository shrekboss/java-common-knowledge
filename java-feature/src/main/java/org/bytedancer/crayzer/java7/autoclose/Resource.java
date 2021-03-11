package org.bytedancer.crayzer.java7.autoclose;

/**
 * @author yizhe.chen
 */
public class Resource implements AutoCloseable{

    public void sayHello() {
        System.out.println("Resource say hello");
    }

    @Override
    public void close() throws Exception {
        System.out.println("Resource is closed");
    }
}
