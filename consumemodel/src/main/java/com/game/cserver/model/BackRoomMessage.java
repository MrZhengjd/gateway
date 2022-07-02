package com.game.cserver.model;

import com.game.common.model.MessageType;
import com.game.common.model.anno.DefaultGameMessage;
import com.game.common.model.anno.HeaderAnno;
import com.game.common.model.msg.BaseChuPaiInfo;
import com.game.common.model.msg.RequestMessageType;
import lombok.Getter;
import lombok.Setter;

/**
 * @author zheng
 */
@HeaderAnno(serviceId = RequestMessageType.BACKTOROOM_MESSAGE,messageType = MessageType.PUSH)
@Getter
@Setter
public class BackRoomMessage extends DefaultGameMessage {
    private BaseChuPaiInfo baseChuPaiInfo;
}
