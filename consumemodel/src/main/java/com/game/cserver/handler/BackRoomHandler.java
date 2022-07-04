package com.game.cserver.handler;


//import com.game.common.messagedispatch.GameDispatchService;
//import com.game.common.messagedispatch.GameMessageListener;
//import com.game.common.model.anno.DynamicRegisterGameService;
//import com.game.common.model.msg.BaseChuPaiInfo;
import com.game.common.eventdispatch.DynamicRegisterGameService;
import com.game.common.redis.JsonRedisManager;
import com.game.common.serialize.DataSerialize;
import com.game.common.serialize.DataSerializeFactory;
import com.game.cserver.config.CommonConsumeConfig;
import com.game.cserver.find.FindPlayerIdProxy;
import com.game.cserver.messagedispatch.GameDispatchService;
import com.game.cserver.messagedispatch.GameMessageListener;
import com.game.cserver.model.BackRoomMessage;
import com.game.cserver.model.FastPlayerRoomHolder;
import com.game.cserver.send.SendMessageModel;
import com.game.domain.checkhu.DefaultCheckHu;
import com.game.domain.checkhu.DefaultCheckHuPools;
import com.game.domain.model.msg.BaseChuPaiInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 上下线处理器
 * @author zheng
 */
@GameDispatchService
@Service
public class BackRoomHandler {
    @Autowired
    private SendMessageModel sendMessageModel;
    @Autowired
    private DynamicRegisterGameService dynamicRegisterGameService;

    @Autowired
    private CommonConsumeConfig commonConsumeConfig;

    private DataSerialize dataSerialize = DataSerializeFactory.getInstance().getDefaultDataSerialize();
    @Autowired
    private JsonRedisManager jsonRedisManager;
    @Autowired
    private FindPlayerIdProxy findPlayerIdProxy;
    @Autowired
    private DefaultCheckHuPools defaultCheckHuPools;
    @GameMessageListener(value = BackRoomMessage.class,name = "${game.common.config.backRoomAppend}",onUsed = "${game.common.config.backRoomOnUsed}" )
    public void handleOnline(BackRoomMessage backRoomMessage){
        if (FastPlayerRoomHolder.getRuntimeContext() == null){
            return;
        }
//        MjCheckHu mjCheckHu = new MjCheckHu();
//        mjCheckHu.checkHu();
        BaseChuPaiInfo info =(BaseChuPaiInfo) backRoomMessage.deserialzeToData();
        DefaultCheckHu checkHuModel = defaultCheckHuPools.getCheckHuModel(commonConsumeConfig.getGameType());
//        checkHuModel.checkHu();
    }




}
