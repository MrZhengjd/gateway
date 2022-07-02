package com.game.common.model.anno;


import com.game.common.model.MessageType;
import com.game.common.model.msg.BaseChuPaiInfo;
import com.game.common.model.msg.RequestMessageType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author zheng
 */
@Getter
@Setter
@HeaderAnno(serviceId = RequestMessageType.CHUPAI_MESSAGE,messageType = MessageType.RPCREQUEST)
public class ChuPaiMessage extends DefaultGameMessage {
    private BaseChuPaiInfo chuPaiInfo;
    @Override
    protected Class getBodyObjClass() {
        return BaseChuPaiInfo.class;
    }
}
