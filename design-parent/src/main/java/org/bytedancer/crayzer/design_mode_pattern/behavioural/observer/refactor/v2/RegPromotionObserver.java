package org.bytedancer.crayzer.design_mode_pattern.behavioural.observer.refactor.v2;

import org.bytedancer.crayzer.design_mode_pattern.behavioural.observer.original.PromotionService;

public class RegPromotionObserver implements RegObserver {
    private PromotionService promotionService; // 依赖注入

    @Override
    public void handleRegSuccess(long userId) {
        promotionService.issueNewUserExperienceCash(userId);
    }
}
