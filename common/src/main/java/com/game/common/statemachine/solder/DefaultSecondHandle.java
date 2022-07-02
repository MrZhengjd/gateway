package com.game.common.statemachine.solder;

/**
 * @author zheng
 */
public class DefaultSecondHandle implements SecondHandle {
    @Override
    public void handEnter(StateChange stateChange, Play play) {
        System.out.println("second handle enter-------");
    }

    @Override
    public void handSwitch(StateChange stateChange, Play play) {
        System.out.println("second handle switch-------");
    }

    @Override
    public void handleExit(StateChange stateChange, Play play) {
        System.out.println("second handle exit-------");
    }
}
