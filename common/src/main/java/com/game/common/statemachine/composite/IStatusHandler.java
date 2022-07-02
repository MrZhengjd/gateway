package com.game.common.statemachine.composite;

import com.game.common.statemachine.Status;

/**
 * @author zheng
 */
public interface IStatusHandler {
    Status handleStatus(Status status);
}
