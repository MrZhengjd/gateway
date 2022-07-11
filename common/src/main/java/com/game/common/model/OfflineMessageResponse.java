package com.game.common.model;


import com.game.common.constant.RequestMessageType;

/**
 * @author zheng
 */

@HeaderAnno(serviceId = RequestMessageType.OFFLINE_MESSAGE,messageType = MessageType.RPCRESPONSE)
public class OfflineMessageResponse extends DefaultGameMessage {

}
