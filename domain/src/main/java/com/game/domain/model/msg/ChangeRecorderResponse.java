package com.game.domain.model.msg;

import com.game.common.constant.RequestMessageType;
import com.game.common.model.DefaultGameMessage;
import com.game.common.model.DefaultResponseGameMessage;
import com.game.common.model.HeaderAnno;
import com.game.common.model.MessageType;

/**
 * @author zheng
 */
@HeaderAnno(serviceId = RequestMessageType.CHANGE_CORDER,messageType = MessageType.RPCRESPONSE)
public class ChangeRecorderResponse extends DefaultGameMessage {

}
