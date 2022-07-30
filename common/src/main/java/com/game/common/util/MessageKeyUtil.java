package com.game.common.util;

import com.game.common.model.GameMessage;
import com.game.common.model.HeaderAnno;

/**
 * @author zheng
 */
public class MessageKeyUtil {
    public static Object getMessageKey(GameMessage message){
        Object key = message.getHeader().getPlayerId();
        if (message.getHeader().getAttribute() != null){
            key = message.getHeader().getAttribute();
        }
        if (key == null){
            key = 1;
        }
        return key;
    }

}
