package com.game.common.model.msg;

//import com.game.network.rpc.MessageResponse;

import com.game.common.serialize.DataSerialize;
import io.netty.buffer.ByteBuf;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Message implements Serializable {

    private Header header;
//    private Body body;

    private Object body;
//    public Message() {
//    }

//    public Message(Header header, Body body) {
//        this.header = header;
//
//        this.body = body;
//    }
public static Message buildPushRequest(String token){
    Message message = new Message();
    message.setHeader(Header.pushHeader());
//        Body body = new Body();
//        body.setMessage(token);
    message.setBody(token);
    return message;
}
    public static Message buildAuthRequest(String token){
        Message message = new Message();
        message.setHeader(Header.authHeader());

        message.setBody(token);
        return message;
    }

    public static Message buildSendRequest(Object data,int serviceId){
        Message message = new Message();
        message.setHeader(Header.sendHeader());
        message.getHeader().setServiceId(serviceId);
        Body body = new Body();
        body.setMessage(data);
        message.setBody(body);
        return message;
    }
    public static Message buildAuthResponse(int code, Object data){
        Message message = new Message();
        message.setHeader(Header.authHeader());
        MessageResponse response = MessageResponse.build(code,data);
        message.setBody(response);
        return message;
    }

    public static Message buildPongMessage(int code, Object data){
        Message message = new Message();
        message.setHeader(Header.pongHeader());
        MessageResponse response = MessageResponse.build(code,data);
        message.setBody(response);
        return message;
    }

    public static Message buildRpcResponseMessage(Message from,Object result){
        Message message = new Message();
        message.setHeader(Header.rpcResponseHeader());
        MessageResponse response = new MessageResponse(200);
//        response.setPlayerId(from.getBody().getPlayerId());
//        response.setRequestId(from.getBody().getRequestId());
        response.setResult(result);
        message.setBody(response);

        return message;
    }

    public static Message buildPushMessage(int code, Object data){
        Message message = new Message();
        message.setHeader(Header.pushHeader());
        MessageResponse response = MessageResponse.build(code,data);
        message.setBody(response);
        return message;
    }
    public void writeToByteBuf(ByteBuf byteBuf, DataSerialize serialize) {
        if (byteBuf != null){
            Header header = getHeader();
            byteBuf.writeInt(header.getCrcCode());
            byte[] data;
            if (serialize != null){
                data = serialize.serialize(body);
            }else {
                data = body.toString().getBytes();
            }

//            byteBuf.writeInt(header.getSeqId());

            byteBuf.writeInt(data.length);

            byteBuf.writeInt(header.getToServerId());
            byteBuf.writeByte(header.getType());
            byteBuf.writeBytes(data);
//            System.out.println("here is seqId "+header.getSeqId()+ " data "+getBody());
//            System.out.println("readable bytes "+byteBuf.readableBytes());
        }

    }

    public static Message read(byte[] datas, ByteBuf byteBuf){
        if (datas != null){
            byteBuf.capacity(datas.length);
            byteBuf.writeBytes(datas);
            return readFromByte(byteBuf,datas.length);
        }
        return null;
    }
    /**
     * 读取数据
     * @param byteBuf
     * @return
     */
    public static Message readFromByte(ByteBuf byteBuf, int byteLength){
        int seqId = 0;
        if (byteBuf.readableBytes() == 0){
            return null;
        }
        if (byteBuf.readableBytes() != byteLength){
//                byteBuf.release();
            throw new IllegalArgumentException("byte length not wright requere length "+byteLength +" and byte length "+byteBuf.readableBytes() + " seq id "+seqId);
        }
        int code = byteBuf.readInt();
//        seqId = byteBuf.readInt();
//            System.out.println("here is read seqId "+seqId);
        int length = byteBuf.readInt();

        int serverId = byteBuf.readInt();
        byte type = byteBuf.readByte();
        if (length >byteLength ){
//                byteBuf.release();
            throw new IllegalArgumentException("byte length not wright requere length "+length +" and byte length "+byteLength + " seq id "+seqId);
        }
        byte[] bytes = null;
        if (length > 0){
            if (byteBuf.readableBytes() < length){
//                System.out.println("data not right --------"+seqId + "server id "+serverId);
                return null;
            }
            bytes = new byte[length];
            byteBuf.readBytes(bytes);
        }

        Message message = new Message();
        Header header = new Header(type);
        header.setToServerId(serverId);
//        header.setSeqId(seqId);
        header.setLength(length);
        message.setHeader(header);
        Body body = new Body();
        body.setMessage(bytes == null ? null :bytes.toString());
        message.setBody(body);

        return message;
//        try {
//
//        }catch (Exception e){
//            e.printStackTrace();
//            System.out.println("here is seq id "+seqId);
//            return null;
//        }

    }

    public TMessage buildTMessage() {
        TMessage tMessage = new TMessage();
        tMessage.setCrcCode(header.getCrcCode());
        tMessage.setBody(body);
        THeader tHeader = header.buildTHeader();
        tMessage.setTHeader(tHeader);
        return tMessage;
    }
}
