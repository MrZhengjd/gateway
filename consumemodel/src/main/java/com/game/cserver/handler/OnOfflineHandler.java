package com.game.cserver.handler;

import com.game.common.concurrent.PromiseUtil;
import com.game.common.messagedispatch.GameDispatchService;
import com.game.common.messagedispatch.GameMessageListener;
import com.game.common.model.DtoMessage;
import com.game.common.model.anno.DynamicRegisterGameService;
import com.game.common.model.anno.GameMessage;
import com.game.common.model.dtoms.OfflineMessage;
import com.game.common.model.dtoms.OnlineMessage;
import com.game.common.model.msg.THeader;
import com.game.cserver.config.CommonConsumeConfig;
import com.game.cserver.find.FindPlayerIdProxy;
import com.game.cserver.send.SendMessageModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 上下线处理器
 * @author zheng
 */
@GameDispatchService
@Service
public class OnOfflineHandler {
    @Autowired
    private SendMessageModel sendMessageModel;
    @Autowired
    private DynamicRegisterGameService dynamicRegisterGameService;

    @Autowired
    private CommonConsumeConfig commonConsumeConfig;


    @Autowired
    private FindPlayerIdProxy findPlayerIdProxy;
    @GameMessageListener(value = OnlineMessage.class)
    public void handleOnline(OnlineMessage onlineMessage){

        handleOnOffline(commonConsumeConfig.getOnlineSendWay(),onlineMessage.getHeader());
    }

    @GameMessageListener(value = OfflineMessage.class)
    public void handleOffline(OfflineMessage offlineMessage){
        handleOnOffline(commonConsumeConfig.getOfflineSendWay(),offlineMessage.getHeader());
    }

    /**
     * 处理并返回上下线消息
     * @param fingPlayerIdsWay
     * @param tHeader
     */
    private void handleOnOffline(int fingPlayerIdsWay, THeader tHeader){
        List<Long> playerIds = findPlayerIdProxy.findPlayerIds(fingPlayerIdsWay, tHeader.getPlayerId());
        GameMessage response = dynamicRegisterGameService.getResponseToPlayerIdsSimple(tHeader,playerIds);
        sendMessageModel.sendMessageToKafkaSimple(response);
    }
}
