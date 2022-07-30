package com.game.domain.model.msg;

import com.game.common.constant.RequestMessageType;
import com.game.common.model.DefaultGameMessage;
import com.game.common.model.DefaultResponseGameMessage;
import com.game.common.model.HeaderAnno;
import com.game.common.model.MessageType;
import lombok.Getter;
import lombok.Setter;

/**
 * @author zheng
 */
@HeaderAnno(serviceId = RequestMessageType.ASK_MASTER,messageType = MessageType.RPCRESPONSE)
@Getter
@Setter
public class AskMasterResponse extends DefaultResponseGameMessage {
}
