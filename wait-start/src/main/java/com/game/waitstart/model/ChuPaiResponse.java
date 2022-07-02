package com.game.waitstart.model;

import com.game.common.model.MessageType;
import com.game.common.model.anno.AbstractGameMessage;
import com.game.common.model.anno.HeaderAnno;
import com.game.common.model.msg.BaseChuPaiInfo;

/**
 * @author zheng
 */
@HeaderAnno(serviceId = 10001,messageType = MessageType.RPCRESPONSE)
public class ChuPaiResponse extends AbstractGameMessage {
    private BaseChuPaiInfo data;
    @Override
    protected Class getBodyObjClass() {
        return BaseChuPaiInfo.class;
    }
}
