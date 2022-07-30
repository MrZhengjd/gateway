package com.game.common.model;

import com.game.common.constant.RequestMessageType;

/**
 * @author zheng
 */
@HeaderAnno(serviceId = RequestMessageType.BAND_SERVICE,messageType = MessageType.RPCRESPONSE)
public class BandSupplyResponse extends DefaultGameMessage {
}
