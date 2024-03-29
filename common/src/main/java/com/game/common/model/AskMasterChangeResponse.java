package com.game.common.model;

import com.game.common.constant.RequestMessageType;
import lombok.Getter;
import lombok.Setter;

/**
 * @author zheng
 */
@HeaderAnno(serviceId = RequestMessageType.ASK_MASTER_CHANGE,messageType = MessageType.RPCRESPONSE)
@Getter
@Setter
public class AskMasterChangeResponse extends DefaultGameMessage {
    private Long lastCopyId;
}
