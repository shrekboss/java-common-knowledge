package org.bytedancer.crayzer.devmisuse.coding.advancedfeatures.reflectionissue;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ReflectionIssueApplication {

    public static void main(String[] args) throws Exception {
        ReflectionIssueApplication application = new ReflectionIssueApplication();
        application.wrong();
        application.right();
    }

    private void age(int age) {
        log.info("int age = {}", age);
    }

    private void age(Integer age) {
        log.info("Integer age = {}", age);
    }

    // 反射调用方法，是以反射获取方法时传入的方法名称和参数类型来确定调用方法的。
    public void wrong() throws Exception {
        //  - int age = 36
        getClass().getDeclaredMethod("age", Integer.TYPE).invoke(this, Integer.valueOf("36"));
    }

    public void right() throws Exception {
        // - Integer age = 36
        // - Integer age = 36
        getClass().getDeclaredMethod("age", Integer.class).invoke(this, Integer.valueOf("36"));
        getClass().getDeclaredMethod("age", Integer.class).invoke(this, 36);
    }
}
