package org.bytedancer.crayzer.design_mode_principle.ocp.extension;


import org.bytedancer.crayzer.design_mode_principle.ocp.AlertRule;
import org.bytedancer.crayzer.design_mode_principle.ocp.Notification;
import org.bytedancer.crayzer.design_mode_principle.ocp.extension.handler.ErrorAlertHandler;
import org.bytedancer.crayzer.design_mode_principle.ocp.extension.handler.TpsAlertHandler;

public class ApplicationContext {
    private AlertRule alertRule;
    private Notification notification;
    private Alert alert;

    /**
     * 饿汉式单例
     */
    private static final ApplicationContext INSTANCE = new ApplicationContext();

    private ApplicationContext() {
        INSTANCE.initializeBeans();
    }

    public void initializeBeans() {
        //省略一些初始化代码
        alertRule = new AlertRule();
        //省略一些初始化代码
        notification = new Notification();
        alert = new Alert();
        // alert.addAlertHandler(new TimeoutAlertHandler(alertRule, notification));
        alert.addAlertHandler(new TpsAlertHandler(alertRule, notification));
        alert.addAlertHandler(new ErrorAlertHandler(alertRule, notification));
    }

    public Alert getAlert() {
        return alert;
    }

    public static ApplicationContext getSingletonInstance() {
        return INSTANCE;
    }
}
