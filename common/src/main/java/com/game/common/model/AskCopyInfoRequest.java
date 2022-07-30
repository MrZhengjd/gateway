package com.game.common.model;

import com.game.common.constant.RequestMessageType;
import lombok.Getter;
import lombok.Setter;

/**
 * @author zheng
 */
@HeaderAnno(serviceId = RequestMessageType.ASK_COPY_ID,messageType = MessageType.RPCREQUEST)
@Getter
@Setter
public class AskCopyInfoRequest extends DefaultGameMessage {
    @Override
    protected Class getBodyObjClass() {
        return String.class;
    }
}
