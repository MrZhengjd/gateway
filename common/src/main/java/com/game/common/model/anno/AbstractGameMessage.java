package com.game.common.model.anno;

import com.game.common.model.msg.THeader;
import com.game.common.serialize.DataSerialize;
import com.game.common.serialize.DataSerializeFactory;

/**
 * @author zheng
 */
public abstract class AbstractGameMessage<T> implements GameMessage<T> {
    private THeader tHeader;
    private T message;

    private byte[] data;
    private static DataSerialize dataSerialize = DataSerializeFactory.getInstance().getDefaultDataSerialize();
//    public static PooledByteBufAllocator allocator = PooledByteBufAllocator.DEFAULT;
    public AbstractGameMessage() {
        HeaderAnno headerAnno = this.getClass().getAnnotation(HeaderAnno.class);
        if (headerAnno ==null){
            return;
//            throw new IllegalArgumentException("还没添加注解");
        }

        tHeader = new THeader(headerAnno.messageType().value);
        tHeader.setModuleId(headerAnno.serviceId());
//        tHeader.set(headerAnno.serverId());
    }



    @Override
    public void copyHeadData(THeader header) {
        this.tHeader.setPlayerId(header.getPlayerId());
        this.tHeader.setTraceId(header.getTraceId());
    }

    @Override
    public byte[] getData() {
        if (data == null){
            data = dataSerialize.serialize(message);
        }
        return data;
    }


    @Override
    public void readBody(byte[] data) {
      this.data = data;
    }


    @Override
    public void readHeader(byte[] header) {
        if (header != null){
            this.tHeader = dataSerialize.deserialize(header,THeader.class);
        }
    }

    @Override
    public T deserialzeToData() {
        return dataSerialize.deserialize(data,getBodyObjClass());
    }

    public T getMessage() {
        return message;
    }
    protected abstract Class<T> getBodyObjClass();

    public void setMessage(T message) {
        this.message = message;
    }

    @Override
    public void setMessageData(T body) {
        setMessage(body);
        if (body != null){
            this.data = dataSerialize.serialize(body);
        }

    }

    @Override
    public void setHeader(THeader tHeader) {
        this.tHeader = tHeader;
    }


    @Override
    public THeader getHeader() {
        return tHeader;
    }
}
