package com.game.common.model;


import com.game.common.eventdispatch.Event;


import io.netty.channel.ChannelHandlerContext;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zheng
 */
public class DefaultGameMessage<T> extends AbstractGameMessage<T> implements Event {


    public DefaultGameMessage() {

        setHeader(new THeader(MessageType.RPCREQUEST.value));
    }
}
