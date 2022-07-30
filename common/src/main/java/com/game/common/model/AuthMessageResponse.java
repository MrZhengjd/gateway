package com.game.common.model;

import com.game.common.constant.RequestMessageType;
import lombok.Getter;

/**
 * @author zheng
 */
@HeaderAnno(serviceId = RequestMessageType.AUTH_REQUEST,messageType = MessageType.RPCRESPONSE)
public class AuthMessageResponse extends DefaultResponseGameMessage{
}
