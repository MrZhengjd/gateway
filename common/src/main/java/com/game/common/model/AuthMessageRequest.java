package com.game.common.model;

import com.game.common.constant.RequestMessageType;

/**
 * @author zheng
 */
@HeaderAnno(serviceId = RequestMessageType.AUTH_REQUEST,messageType = MessageType.RPCREQUEST)
public class AuthMessageRequest extends DefaultGameMessage{
}
