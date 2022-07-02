package com.game.common.model.msg;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author zheng
 * 消息实体
 */
@Getter
@Setter
public class Body implements Serializable {
    private String messageId;
    private Long playerId;
//    private Long requestTime;
//    private Long responseTime;
    private Object message;
    private Long requestId;

    @Override
    public String toString() {
        return "messageId"+messageId + " message "+ message;
    }
    public static Body build(Message message){
        return new Builder().buildResponse(message);
    }
    private static class Builder{
        private Body buildResponse(Message message){
            Body body = new Body();
            Body tem = (Body) message.getBody();
//            message.getHeader().setRequestId();
            body.setMessageId(tem.getMessageId());
//            body.setRequestTime(tem.getRequestTime());
            return body;
        }
    }
}
