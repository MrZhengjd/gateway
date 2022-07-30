package com.game.diststore.util;

import com.game.common.constant.RequestMessageType;
import com.game.common.eventdispatch.DynamicRegisterGameService;
import com.game.common.model.GameMessage;
import com.game.common.model.ResponseVo;
import com.game.network.cache.ChannelMap;

/**
 * @author zheng
 */
public class HandleUtil {
    /**
     * 处理并发送到来的玩家去
     * @param request
     * @param responseData
     */
    public static void handleAndSendToRequestPlayer(GameMessage request, Object responseData, DynamicRegisterGameService dynamicRegisterGameService, ChannelMap channelMap,boolean success){
        GameMessage response;
        if (request.getHeader().getServiceId() < RequestMessageType.ASK_MASTER_CHANGE){
            response = dynamicRegisterGameService.getResponseInstanceByMessageId(request.getHeader().getServiceId());
        }else {
            response = dynamicRegisterGameService.getDefaultResponse(request.getHeader().getServiceId());
        }

//        GameMessage response = dynamicRegisterGameService.getResponseInstanceByMessageId(request.getHeader().getServiceId());
        if (success){
            response.setMessageData(ResponseVo.success(responseData));
        }else {
            response.setMessageData(ResponseVo.fail(responseData));
        }

        response.getHeader().setPlayerId(request.getHeader().getPlayerId());
        channelMap.writeResponse(response);
    }
}
