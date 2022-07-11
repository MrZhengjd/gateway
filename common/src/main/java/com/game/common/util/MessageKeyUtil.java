package com.game.common.util;

import com.game.common.model.GameMessage;

/**
 * @author zheng
 */
public class MessageKeyUtil {
    public static Object getMessageKey(GameMessage message){
        Object key = message.getHeader().getPlayerId();
        if (message.getHeader().getAttribute() != null){
            key = message.getHeader().getAttribute();
        }
        return key;
    }
}
