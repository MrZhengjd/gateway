package com.game.common.model;


import com.game.common.constant.RequestMessageType;

/**
 * @author zheng
 */
//@Component
@HeaderAnno(serviceId = RequestMessageType.ONLINE_MESSAGE,messageType = MessageType.RPCRESPONSE)

public class OnlineMessageResponse extends DefaultGameMessage {

    public OnlineMessageResponse() {

//        System.out.println("herei -----------");
    }
}
