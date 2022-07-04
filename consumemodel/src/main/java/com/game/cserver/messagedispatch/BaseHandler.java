package com.game.cserver.messagedispatch;


import com.game.domain.model.anno.GameMessage;

/**
 * @author zheng
 */
public interface BaseHandler {
    void handlerRequest(GameMessage gameMessage);
}
