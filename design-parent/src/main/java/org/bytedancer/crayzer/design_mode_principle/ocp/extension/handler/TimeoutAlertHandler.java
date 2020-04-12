package org.bytedancer.crayzer.design_mode_principle.ocp.extension.handler;

import org.bytedancer.crayzer.design_mode_principle.ocp.AlertRule;
import org.bytedancer.crayzer.design_mode_principle.ocp.Notification;
import org.bytedancer.crayzer.design_mode_principle.ocp.NotificationEmergencyLevel;
import org.bytedancer.crayzer.design_mode_principle.ocp.extension.vo.ApiStatInfo;

public class TimeoutAlertHandler extends AlertHandler {

    public TimeoutAlertHandler(AlertRule rule, Notification notification) {
        super(rule, notification);
    }

    @Override
    public void check(ApiStatInfo apiStatInfo) {
        if (apiStatInfo.getErrorCount() > rule.getMatchedRule(apiStatInfo.getApi()).getMaxTimeoutCount()) {
            notification.notify(NotificationEmergencyLevel.URGENCY, "...");
        }
    }
}
