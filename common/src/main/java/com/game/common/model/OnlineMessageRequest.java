package com.game.common.model;


import com.game.common.constant.RequestMessageType;

/**
 * @author zheng
 */
//@Component
@HeaderAnno(serviceId = RequestMessageType.ONLINE_MESSAGE,messageType = MessageType.RPCREQUEST)

public class OnlineMessageRequest extends DefaultGameMessage {

    public OnlineMessageRequest() {

        System.out.println("herei -----------");
    }
}
