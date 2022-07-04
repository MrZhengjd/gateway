package com.game.gateway.model;



import com.game.common.model.DefaultGameMessage;
import com.game.domain.model.msg.BaseChuPaiInfo;
import lombok.Getter;
import lombok.Setter;

/**
 * @author zheng
 */
@Getter
@Setter
public class ChuPaiMessage extends DefaultGameMessage {
    private BaseChuPaiInfo chuPaiInfo;
    @Override
    protected Class getBodyObjClass() {
        return BaseChuPaiInfo.class;
    }
}
