package com.game.waitstart.consumer;


import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySource;
import com.game.common.constant.InfoConstant;
import com.game.cserver.consume.ConsumerModel;
import com.game.cserver.messagedispatch.GameMessageDispatchService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

/**
 * @author zheng
 */
@Component
@NacosPropertySource(dataId = "nacoswaitstart",autoRefreshed = true)
public class ConsumerAndSendMessage {
    @Autowired
    private DynamicRegisterGameService dynamicRegisterGameService;

    @Autowired
    private GameMessageDispatchService gameMessageDispatchService;
    @Autowired
    private ConsumerModel consumerModel;
    @KafkaListener(topics = {"${name:waitstart1}" }, groupId = "test1" )
    public void backupHandle(ConsumerRecord<String ,byte[]> record,Acknowledgment acknowledgment){
//
        consumerModel.consumeMessageToDispatch(record,InfoConstant.GATEWAY_LOGIC_TOPIC,acknowledgment);
    }

    public static String generateTopic(String prefix,int serverId) {
        return prefix + "-" + serverId;
    }
//
}
