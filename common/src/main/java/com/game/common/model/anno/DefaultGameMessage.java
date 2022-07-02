package com.game.common.model.anno;


import com.game.common.eventdispatch.Event;
import com.game.common.flow.model.Response;
import com.game.common.model.MessageType;
import com.game.common.model.msg.THeader;
import io.netty.channel.ChannelHandlerContext;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zheng
 */
public class DefaultGameMessage extends AbstractGameMessage implements Event {
    @Override
    protected Class getBodyObjClass() {
        return Response.class;
    }
    public static void sendErrorServerMessage(ChannelHandlerContext ctx){
        Map<String ,Object> data = new HashMap<>();
        data.put("code",408);
        data.put("status",false);
        sendInfo(ctx,data);

    }
    public static void sendInfo(ChannelHandlerContext ctx,Object data){
        DefaultGameMessage result = new DefaultGameMessage();
//        result.setBody(data);
        ctx.channel().writeAndFlush(result);
    }

    public DefaultGameMessage() {

        setHeader(new THeader(MessageType.RPCREQUEST.value));
    }
}
