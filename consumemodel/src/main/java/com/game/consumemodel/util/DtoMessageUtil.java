package com.game.consumemodel.util;

import com.game.common.eventdispatch.DynamicRegisterGameService;
import com.game.common.model.GameMessage;
import com.game.common.model.THeader;
import com.game.common.serialize.DataSerialize;
import com.game.common.serialize.DataSerializeFactory;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * @author zheng
 */
public class DtoMessageUtil {
    private static DataSerialize dataSerialize =  DataSerializeFactory.getInstance().getDefaultDataSerialize();
    public static GameMessage readMessageHeader(byte[] datas, DynamicRegisterGameService dynamicRegisterGameService){

        ByteBuf byteBuf = Unpooled.wrappedBuffer(datas);
        int headerLength = byteBuf.readInt();
        int dataLength = byteBuf.readInt();
        byte[] head = new byte[headerLength];
        byte[] data = new byte[dataLength];
        byteBuf.readBytes(head);
        byteBuf.readBytes(data);
        THeader h =dataSerialize.deserialize(head, THeader.class);
        GameMessage gameMessage = dynamicRegisterGameService.getRequestInstanceByMessageId(h.getServiceId());
        gameMessage.setHeader(h);
        gameMessage.readBody(data);
        return gameMessage;
    }
}
