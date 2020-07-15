package org.bytedancer.crayzer.design_mode_pattern.behavioural.state.stateMachine.state.v2;


import org.bytedancer.crayzer.design_mode_pattern.behavioural.state.stateMachine.branchingLogic.State;

public interface IMario {
    State getName();
    void obtainMushRoom(MarioStateMachine stateMachine);
    void obtainCape(MarioStateMachine stateMachine);
    void obtainFireFlower(MarioStateMachine stateMachine);
    void meetMonster(MarioStateMachine stateMachine);
}

// 省略SuperMario、CapeMario、FireMario类...

