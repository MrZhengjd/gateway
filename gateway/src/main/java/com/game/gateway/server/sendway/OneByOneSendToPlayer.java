package com.game.gateway.server.sendway;

import com.game.common.annotation.BandKey;
import com.game.common.constant.InfoConstant;

import com.game.domain.model.anno.GameMessage;
import com.game.newwork.cache.ChannleMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author zheng
 */
@Component
@BandKey(key = InfoConstant.ONY_BY_ONE)
public class OneByOneSendToPlayer implements SendToPlayer {
    private Logger logger = LoggerFactory.getLogger(OneByOneSendToPlayer.class);
    @Override
    public void sendToPlayer(GameMessage message, ChannleMap channleMap) {
       SendMessageUtil.sendMessage(message,channleMap,message.getHeader().getPlayerId());
    }
}
