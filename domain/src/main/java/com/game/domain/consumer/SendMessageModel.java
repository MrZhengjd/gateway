package com.game.domain.consumer;

import com.game.common.model.GameMessage;

/**
 * @author zheng
 */
public interface SendMessageModel {
    public void sendMessageToMq(GameMessage data, String baseTopic, Object key);


}
