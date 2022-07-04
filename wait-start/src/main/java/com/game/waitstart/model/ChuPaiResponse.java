package com.game.waitstart.model;


import com.game.common.model.AbstractGameMessage;
import com.game.domain.model.msg.BaseChuPaiInfo;

/**
 * @author zheng
 */
public class ChuPaiResponse extends AbstractGameMessage {
    private BaseChuPaiInfo data;
    @Override
    protected Class getBodyObjClass() {
        return BaseChuPaiInfo.class;
    }
}
