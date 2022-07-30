package com.game.domain.model.msg;

import com.game.common.constant.RequestMessageType;
import com.game.common.model.DefaultGameMessage;
import com.game.common.model.HeaderAnno;
import com.game.common.model.MessageType;
import com.game.domain.model.vo.ChangeVo;
import lombok.Getter;
import lombok.Setter;

/**
 * @author zheng
 */
@HeaderAnno(serviceId = RequestMessageType.CHANGE_CORDER,messageType = MessageType.RPCREQUEST)
@Getter
@Setter
public class ChangeRecorderRequest extends DefaultGameMessage {
    @Override
    protected Class getBodyObjClass() {
        return ChangeVo.class;
    }

//    private ChangeVo changeVo;
}
