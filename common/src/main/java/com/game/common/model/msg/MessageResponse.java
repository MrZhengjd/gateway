package com.game.common.model.msg;


import java.io.Serializable;

public class MessageResponse extends Body implements Serializable {
    private int code;
//    private boolean success;
    private String error;
    private Object result;

    public MessageResponse(int code) {
        this.code = code;
    }

    public MessageResponse(int code, Object result) {
        this.code = code;
        this.result = result;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    private MessageResponse(Message message){
//        this.setMessageId(message.getBody().getMessageId());
//        this.setRequestId(message.getBody().getRequestId());
//        this.setRequestTime(message.getBody().getRequestTime());
    }

//    public boolean isSuccess() {
//        return success;
//    }
//
//    public void setSuccess(boolean success) {
//        this.success = success;
//    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
    public static MessageResponse build(Message message){
        return new Builder().buildResponse(message);
    }
    public static MessageResponse build(int code,Object message){
        return Builder.buildRespone(code,message);
    }
    private static class Builder{
        private MessageResponse buildResponse(Message message){
            return new MessageResponse(message);
        }
        public static MessageResponse buildRespone(int code,Object message){
            return new MessageResponse(code,message);
        }
    }

//    @Override
//    public String toString() {
//        return "result "+result.toString() +"is sucees "+success +" error "+error;
//    }
}
