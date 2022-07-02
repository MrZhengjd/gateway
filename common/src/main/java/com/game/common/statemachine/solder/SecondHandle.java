package com.game.common.statemachine.solder;

/**
 * @author zheng
 */
public interface SecondHandle {
    public void handEnter(StateChange stateChange, Play play);

    public void handSwitch(StateChange stateChange, Play play);

    public void handleExit(StateChange stateChange, Play play);
}
