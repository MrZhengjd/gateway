package com.game.common.messagedispatch;

import com.game.common.model.anno.GameMessage;

/**
 * @author zheng
 */
public interface BaseHandler {
    void handlerRequest(GameMessage gameMessage);
}
