package com.game.gateway.server.sendway;

import com.game.domain.model.anno.GameMessage;
import com.game.newwork.cache.ChannleMap;

/**
 * @author zheng
 */
public interface SendToPlayer {
    void sendToPlayer(GameMessage message, ChannleMap channleMap);
}
