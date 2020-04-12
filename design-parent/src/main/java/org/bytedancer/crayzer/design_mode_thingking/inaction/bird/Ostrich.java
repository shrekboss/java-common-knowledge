package org.bytedancer.crayzer.design_mode_thingking.bird;

import org.bytedancer.crayzer.design_mode_thingking.inaction.bird.EggLayAbility;
import org.bytedancer.crayzer.design_mode_thingking.inaction.bird.EggLayable;
import org.bytedancer.crayzer.design_mode_thingking.inaction.bird.TweetAbility;
import org.bytedancer.crayzer.design_mode_thingking.inaction.bird.Tweetable;

public class Ostrich implements Tweetable, EggLayable { // 鸵鸟
    //组合
    private TweetAbility tweetAbility = new TweetAbility();
    private EggLayAbility eggLayAbility = new EggLayAbility();

    @Override
    public void layEgg() {
        eggLayAbility.layEgg();
    }

    @Override
    public void tweet() {
        tweetAbility.tweet();
    }
}
