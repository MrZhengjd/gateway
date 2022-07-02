package com.game.common.model.msg;




//import com.game.network.coder.MessageType;



import com.game.common.model.MessageType;

import java.io.Serializable;

public class Header implements Serializable {

    private final int crcCode;


    public Header(int length, byte type) {
        this.crcCode = ConstantValue.HEAD_START;
        this.type = type;
        this.length = length;
//        this.seqId = seqId;
    }

    public Header(int length, byte type, int fromServerId) {
        this.length = length;
        this.type = type;
        this.fromServerId = fromServerId;
        this.crcCode = ConstantValue.HEAD_START;
    }

    @Override
    public String toString() {
        return "Header{" +
                "crcCode=" + crcCode +
                ", length=" + length +
                ", type=" + type +
                '}';
    }

    private int length;//消息长度
//    private int traceId;
    private int toServerId;
    private int fromServerId;
    private int serviceId;
    //上游调用方id
    private int callAppId;

    public int getFromServerId() {
        return fromServerId;
    }

    public void setFromServerId(int fromServerId) {
        this.fromServerId = fromServerId;
    }

    public int getCallAppId() {
        return callAppId;
    }

    public void setCallAppId(int callAppId) {
        this.callAppId = callAppId;
    }

//    public int getTraceId() {
//        return traceId;
//    }
//
//    public void setTraceId(int traceId) {
//        this.traceId = traceId;
//    }

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public int getToServerId() {
        return toServerId;
    }

    public void setToServerId(int toServerId) {
        this.toServerId = toServerId;
    }

    public void setLength(int length) {
        this.length = length;
    }

    private final byte type;//消息类型
//    private  int seqId;
//
//    public int getSeqId() {
//        return seqId;
//    }
//
//    public void setSeqId(int seqId) {
//        this.seqId = seqId;
//    }

//    private Header(byte type,int seqId) {
//        this.crcCode = ConstantValue.HEAD_START;
//        this.type = type;
////        this.seqId = seqId;
//    }
    public Header(byte type) {
        this.crcCode = ConstantValue.HEAD_START;
        this.type = type;

    }

    public int getCrcCode() {
        return crcCode;
    }

    public int getLength() {
        return length;
    }


    public byte getType() {
        return type;
    }

    public THeader buildTHeader() {
        THeader tHeader = new THeader(type);
        tHeader.setCallAppId(callAppId);
        tHeader.setServerId(toServerId);
        tHeader.setModuleId(serviceId);
//        tHeader.setTraceId(traceId);
        return tHeader;
    }

    private static class Builder{
        public static Header buildHeader(byte type,int requestId){
            return new Header(type);
        }
        public static Header buildHeader(byte type){
            return new Header(type);
        }
    }
    public static Header rpcRequestHeader(int requestId){
        return Builder.buildHeader(MessageType.RPCREQUEST.value,requestId);
    }
    public static Header rpcRequestHeader(){
        return Builder.buildHeader(MessageType.RPCREQUEST.value);
    }

    public static Header rpcResponseHeader(){
        return Builder.buildHeader(MessageType.RPCRESPONSE.value);
    }
    public static Header authHeader(){
        return Builder.buildHeader(MessageType.AUTH.value);
    }

    public static Header sendHeader(){
        return Builder.buildHeader(MessageType.SEND.value);
    }
    public static Header pushHeader(){
        return Builder.buildHeader(MessageType.PUSH.value);
    }
    public static Header pongHeader(){
        return Builder.buildHeader(MessageType.PONG.value);
    }
}
