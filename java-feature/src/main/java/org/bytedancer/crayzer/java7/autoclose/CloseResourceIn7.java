package org.bytedancer.crayzer.java7.autoclose;

/**
 * @author yizhe.chen
 */
public class CloseResourceIn7 {

    public static void main(String[] args) {
        try(Resource resource = new Resource(); Resource2 resource2 = new Resource2()) {
            resource.sayHello();
            resource2.sayHello();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
