package com.game.common.statemachine;

/**
 * @author zheng
 */
public interface StatusMachine {
    public Status getNextStatus(Status status, StateEvent event);
}
