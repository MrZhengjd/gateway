package com.game.waitstart.diamond;


import com.game.common.constant.InfoConstant;
import com.game.common.eventdispatch.DynamicRegisterGameService;
import com.game.common.model.GameMessage;
import com.game.common.serialize.DataSerialize;
import com.game.common.serialize.DataSerializeFactory;
import com.game.common.util.TopicUtil;
import com.game.domain.consume.SendMessageModel;
import com.game.domain.messagedispatch.GameDispatchService;
import com.game.domain.messagedispatch.GameMessageListener;
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
        String topic = TopicUtil.generateTopic(InfoConstant.GATEWAY_LOGIC_TOPIC, chuPaiMessage.getHeader().getTraceId());// 动态创建与业务服务交互的消息总线Topic
        sendMessageModel.sendMessageToMq(response, topic,chuPaiMessage.getHeader().getAttribute());
        System.out.println("welcome dispatch --------------------");
    }
}
