package com.game.common.model;

import com.game.common.constant.RequestMessageType;
import lombok.Getter;
import lombok.Setter;

/**
 * @author zheng
 */
@HeaderAnno(serviceId = RequestMessageType.COPY_DATA,messageType = MessageType.RPCREQUEST)
@Getter
@Setter
public class CopyMessageRequest extends DefaultGameMessage {

    @Override
    protected Class getBodyObjClass() {
        return Long.class;
    }
}
