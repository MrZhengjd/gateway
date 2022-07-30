package com.game.common.model;

import com.game.common.constant.RequestMessageType;

/**
 * @author zheng
 */
@HeaderAnno(serviceId = RequestMessageType.COPY_DATA,messageType = MessageType.RPCRESPONSE)
public class CopyMessageResponse extends DefaultResponseGameMessage {
}
