package org.bytedancer.crayzer.common_dev_error.coding.advancedfeatures.reflectionissue;

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

    // 使用反射时的误区是，认为反射调用方法还是根据入参确定方法重载
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
