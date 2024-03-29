package com.game.common.model;

import com.game.common.constant.RequestMessageType;
import lombok.Getter;
import lombok.Setter;

/**
 * @author zheng
 */
@HeaderAnno(serviceId = RequestMessageType.ASK_MASTER_CHANGE,messageType = MessageType.RPCREQUEST)
@Getter
@Setter
public class AskMasterChangeRequest extends DefaultGameMessage {
    private Long lastCopyId = 0l;

}
