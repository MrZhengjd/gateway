package com.game.common.model;

import com.game.common.constant.RequestMessageType;
import com.game.common.model.vo.BandServerVo;
import lombok.Getter;
import lombok.Setter;

/**
 * @author zheng
 */
@HeaderAnno(serviceId = RequestMessageType.BAND_SERVICE,messageType = MessageType.RPCREQUEST)
@Getter
@Setter
public class BandSupplyRequest extends DefaultGameMessage {
//    private BandServerVo bandServerVo;

}
