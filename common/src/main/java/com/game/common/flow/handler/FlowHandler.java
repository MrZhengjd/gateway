package com.game.common.flow.handler;

import com.game.common.flow.model.GameFlow;
import com.game.common.flow.model.Response;
import com.game.common.model.PlayerRequest;

/**
 * @author zheng
 */
public interface FlowHandler {
    Response handlePlayerRequest(PlayerRequest playerRequest, GameFlow gameFlow);
}
