package com.game.gateway.server.sendway;

import com.game.common.annotation.BandKey;
import com.game.common.constant.InfoConstant;

import com.game.common.model.GameMessage;
import com.game.network.cache.ChannleMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author zheng
 */
@Component
@BandKey(key = InfoConstant.ONY_BY_MANY)
public class OneByManySendToPlayer implements SendToPlayer {
    private Logger logger = LoggerFactory.getLogger(OneByManySendToPlayer.class);
    @Override
    public void sendToPlayer(GameMessage message, ChannleMap channleMap) {
        List<Long> toPlayerIds = message.getHeader().getToPlayerIds();
        for (Long playerId : toPlayerIds){
            SendMessageUtil.sendMessage(message,channleMap,playerId);
        }
    }
}
