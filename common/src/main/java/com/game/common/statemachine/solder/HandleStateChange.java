package com.game.common.statemachine.solder;

/**
 * @author zheng
 */
public interface HandleStateChange {
    void enterState(StateChange stateChange, Play play);
    void switchState(StateChange stateChange, Play play);
    void exitState(StateChange stateChange, Play play);
    void noneState(StateChange stateChange, Play play);
}
