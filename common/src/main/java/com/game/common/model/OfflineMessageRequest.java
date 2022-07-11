package com.game.common.model;


import com.game.common.constant.RequestMessageType;

/**
 * @author zheng
 */

@HeaderAnno(serviceId = RequestMessageType.OFFLINE_MESSAGE,messageType = MessageType.RPCREQUEST)
public class OfflineMessageRequest extends DefaultGameMessage {

}
