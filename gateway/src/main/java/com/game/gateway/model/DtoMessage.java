package com.game.gateway.model;

import com.game.common.model.anno.DefaultGameMessage;
import com.game.common.model.anno.GameMessage;
import com.game.common.model.msg.Header;
import com.game.common.model.msg.Message;
import com.game.common.model.msg.THeader;
import com.game.common.model.msg.TMessage;
import com.game.common.model.vo.TMessageVo;
import com.game.common.serialize.DataSerialize;
import com.game.common.serialize.DataSerializeFactory;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.Unpooled;

/**
 * @author zheng
 */

public class DtoMessage {

    private static PooledByteBufAllocator allocator = PooledByteBufAllocator.DEFAULT;




    private static DataSerialize serializeUtil = DataSerializeFactory.getInstance().getDefaultDataSerialize();


    public DtoMessage(TMessageVo message) {
    }

    public static byte[] serializeData(GameMessage gameMessage){
        ByteBuf byteBuf = allocator.directBuffer();
        writeToByteBuf(byteBuf,serializeUtil,gameMessage);
        int length = byteBuf.readableBytes();
//        System.out.println("out readable bytes "+byteBuf.readableBytes());
        if (length > 0) {
//            System.out.println("here is coming ======================");
            byte[] datas = new byte[length];
            byteBuf.readBytes(datas);
            byteBuf.release();
            return datas;
        }
        byteBuf.release();
        return null;
    }
    public static GameMessage readMessageHeader(byte[] datas){
        GameMessage gameMessage = new DefaultGameMessage();
        ByteBuf byteBuf = Unpooled.wrappedBuffer(datas);
        int headerLength = byteBuf.readInt();
        int dataLength = byteBuf.readInt();
        byte[] head = new byte[headerLength];
        byte[] data = new byte[dataLength];
        byteBuf.readBytes(head);
        byteBuf.readBytes(data);
        gameMessage.setHeader(serializeUtil.deserialize(head, THeader.class));
        gameMessage.readBody(data);
        return gameMessage;
    }

//    private static DtoMessage readFromByte(ByteBuf byteBuf) {
//        int headLength = byteBuf.readInt();
//        int dataLength = byteBuf.readInt();
//        byte[] head = byteBuf.readBytes(headLength).array();
//        byte[] data = byteBuf.readBytes(dataLength).array();
//        DtoHeader header = serializeUtil.deserialize(head, DtoHeader.class);
//        return new DtoMessage(header,data);
//    }

    private static void writeToByteBuf(ByteBuf byteBuf, DataSerialize dataSerialize,GameMessage gameMessage) {

        byte[] headData = dataSerialize.serialize(gameMessage.getHeader());
        byte[] bodyData = gameMessage.getData();
        byteBuf.writeInt(headData.length);
        byteBuf.writeInt(bodyData.length);
        byteBuf.writeBytes(headData);
        byteBuf.writeBytes(bodyData);
    }
}
