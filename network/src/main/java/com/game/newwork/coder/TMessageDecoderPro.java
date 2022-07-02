package com.game.newwork.coder;


import com.game.common.model.anno.DefaultGameMessage;
import com.game.common.model.anno.GameMessage;
import com.game.common.model.msg.THeader;
import com.game.common.relation.Constants;
import com.game.common.serialize.*;
import com.game.common.util.IncommingCount;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

//@ChannelHandler.Sharable
public class TMessageDecoderPro extends ByteToMessageDecoder {
    private static Logger logger = LoggerFactory.getLogger(TMessageDecoderPro.class);
    private DataSerialize serializeUtil = DataSerializeFactory.getInstance().getDefaultDataSerialize();
    private final static int HEADER_LENGTH = 16;
    private final static int MAX_LENGTH = 65535;

    public TMessageDecoderPro() {
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        try {
            if (in.readableBytes() < HEADER_LENGTH) {
                in.clear();
                return;
            }
            if (in.readableBytes() > MAX_LENGTH) {
                in.skipBytes(in.readableBytes());
                return;
            }
            int beginReader;
            while (true) {
                beginReader = in.readerIndex();
                in.markReaderIndex();
                if (in.readInt() == Constants.HEAD_START) {
                    break;
                }

                in.resetReaderIndex();
                in.readByte();

                if (in.readableBytes() < HEADER_LENGTH) {
                    return;
                }
            }
            int headLength = in.readInt();
            int bodyLength = in.readInt();

            int endData = in.readInt();
            if (endData !=Constants. END_TAIL){
                logger.error("receive data not write");
                return;
            }
//
            if (in.readableBytes() < headLength+bodyLength)
            {
                in.resetReaderIndex();
                return;
            }


            byte[] header = new byte[headLength];
            in.readBytes(header);
            byte[] data = new byte[bodyLength];
            in.readBytes(data);
//
            GameMessage gameMessage = new DefaultGameMessage();
            gameMessage.setHeader(serializeUtil.deserialize(header, THeader.class));
            gameMessage.readBody(data);
            out.add(gameMessage);
            IncommingCount.getAndIncrementDecode();
//            in.resetReaderIndex();
        } catch (Exception e) {
            logger.error("decode error " + e,e);
        }
    }


//    @Override
//    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
//        if (in.readableBytes() < HEADER_LENGTH) {
//            in.clear();
//            return;
//        }
//        if (in.readableBytes() > MAX_LENGTH) {
//            in.skipBytes(in.readableBytes());
//            return;
//        }
//        byte[] body = new byte[in.readableBytes()];
//        out.add(serializeUtil.deserialize(body, MessageVo.class));
//    }
}
