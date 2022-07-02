package com.game.common.statemachine.composite;

import com.game.common.statemachine.StateEvent;
import com.game.common.statemachine.Status;

/**
 * @author zheng
 */
public interface StateEventHandler {
    IStatus handleStateEvent(StateEvent event);
}
