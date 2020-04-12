package org.bytedancer.crayzer.design_mode_thingking.bird;

import org.bytedancer.crayzer.design_mode_thingking.inaction.bird.*;

public class Sparrow implements Flyable, Tweetable, EggLayable { // 麻雀

    private TweetAbility tweetAbility = new TweetAbility();
    private EggLayAbility eggLayAbility = new EggLayAbility();
    private FlyAbility flyAbility = new FlyAbility();

    @Override
    public void layEgg() {
        eggLayAbility.layEgg();
    }

    @Override
    public void fly() {
        flyAbility.fly();
    }

    @Override
    public void tweet() {
        tweetAbility.tweet();
    }
}
