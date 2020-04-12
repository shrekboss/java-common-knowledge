package org.bytedancer.crayzer.design_mode_principle.ocp.extension.handler;

import org.bytedancer.crayzer.design_mode_principle.ocp.AlertRule;
import org.bytedancer.crayzer.design_mode_principle.ocp.Notification;
import org.bytedancer.crayzer.design_mode_principle.ocp.NotificationEmergencyLevel;
import org.bytedancer.crayzer.design_mode_principle.ocp.extension.vo.ApiStatInfo;

public class TpsAlertHandler extends AlertHandler {
    public TpsAlertHandler(AlertRule rule, Notification notification) {
        super(rule, notification);
    }

    @Override
    public void check(ApiStatInfo apiStatInfo) {
        long tps = apiStatInfo.getRequestCount()/ apiStatInfo.getDurationOfSeconds();
        if (tps > rule.getMatchedRule(apiStatInfo.getApi()).getMaxTps()) {
            notification.notify(NotificationEmergencyLevel.URGENCY, "...");
        }
    }
}
