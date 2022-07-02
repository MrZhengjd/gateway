package com.game.common.model.dtoms;

import com.game.common.model.MessageType;
import com.game.common.model.anno.DefaultGameMessage;
import com.game.common.model.anno.HeaderAnno;
import com.game.common.model.msg.RequestMessageType;

/**
 * @author zheng
 */
@HeaderAnno(serviceId = RequestMessageType.ONLINE_MESSAGE,messageType = MessageType.SEND)
public class OnlineMessage extends DefaultGameMessage {

}
