package com.game.waitstart.diamond;


import com.game.common.serialize.DataSerialize;
import com.game.common.serialize.DataSerializeFactory;
import com.game.cserver.messagedispatch.GameDispatchService;
import com.game.cserver.messagedispatch.GameMessageListener;
import com.game.cserver.send.SendMessageModel;

import com.game.domain.model.anno.GameMessage;
import com.game.domain.model.msg.BaseChuPaiInfo;
import com.game.waitstart.model.ChuPaiMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author zheng
 */
@GameDispatchService
@Service
public class DismondService {
    @Autowired
    private SendMessageModel sendMessageModel;
    @Autowired
    private DynamicRegisterGameService dynamicRegisterGameService;
    private DataSerialize dataSerialize = DataSerializeFactory.getInstance().getDefaultDataSerialize();
    @GameMessageListener(value = ChuPaiMessage.class,name = "323")
    public void handle(ChuPaiMessage chuPaiMessage){
        BaseChuPaiInfo deserialize = (BaseChuPaiInfo) chuPaiMessage.deserialzeToData();
//        BaseChuPaiInfo deserialize = chuPaiMessage.getData();
        GameMessage response = dynamicRegisterGameService.getResponseByMessageIdSimple(chuPaiMessage.getHeader());
        response.setMessageData("hello");
        sendMessageModel.sendMessageToKafkaSimple(response);
        System.out.println("welcome dispatch --------------------");
    }
}
