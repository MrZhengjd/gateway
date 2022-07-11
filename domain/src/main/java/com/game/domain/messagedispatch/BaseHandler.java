package com.game.domain.messagedispatch;


import com.game.common.model.GameMessage;

/**
 * @author zheng
 */
public interface BaseHandler {
    void handlerRequest(GameMessage gameMessage);
}
