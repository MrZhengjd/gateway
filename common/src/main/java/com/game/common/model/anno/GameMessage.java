package com.game.common.model.anno;


import com.game.common.model.msg.THeader;

/**
 * @author zheng
 */
public interface GameMessage<T> {
    byte[] getData();
    void setHeader(THeader tHeader);
    void readBody(byte[] data);
    void setMessageData(T body);
    void readHeader(byte[] header);
    void copyHeadData(THeader tHeader);
    T deserialzeToData();
    THeader getHeader();

}
