package org.bytedancer.crayzer.design_mode_principle.ocp.extension.handler;

import org.bytedancer.crayzer.design_mode_principle.ocp.AlertRule;
import org.bytedancer.crayzer.design_mode_principle.ocp.Notification;
import org.bytedancer.crayzer.design_mode_principle.ocp.NotificationEmergencyLevel;
import org.bytedancer.crayzer.design_mode_principle.ocp.extension.vo.ApiStatInfo;

public class ErrorAlertHandler extends AlertHandler {
    public ErrorAlertHandler(AlertRule rule, Notification notification) {
        super(rule, notification);
    }

    @Override
    public void check(ApiStatInfo apiStatInfo) {
        if (apiStatInfo.getErrorCount() > rule.getMatchedRule(apiStatInfo.getApi()).getMaxErrorCount()) {
            notification.notify(NotificationEmergencyLevel.SEVERE, "...");
        }
    }
}
