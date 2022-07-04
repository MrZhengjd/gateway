package com.game.gateway.server.sendway;


import com.game.domain.model.anno.GameMessage;
import com.game.domain.model.vo.PlayerChannel;
import com.game.newwork.cache.ChannleMap;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zheng
 */
//@Log4j2
public class SendMessageUtil {
    private static Logger logger = LoggerFactory.getLogger(SendMessageUtil.class);
    public static void sendMessage(GameMessage message, ChannleMap channleMap, Long playerId){
        PlayerChannel byPlayerId = channleMap.getByPlayerId(playerId);
        if (byPlayerId == null){
            logger.error("receive data error"+playerId);
            return;
        }

        Channel channel = byPlayerId.getChannel();
        channel.writeAndFlush(message);
    }
}
