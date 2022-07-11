package com.game.gateway.server.sendway;

import com.game.common.model.GameMessage;
import com.game.network.cache.ChannleMap;

/**
 * @author zheng
 */
public interface SendToPlayer {
    void sendToPlayer(GameMessage message, ChannleMap channleMap);
}
