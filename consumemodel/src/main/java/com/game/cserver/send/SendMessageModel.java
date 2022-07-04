package com.game.cserver.send;

import com.game.common.constant.InfoConstant;
//import com.game.common.model.DtoMessage;
//import com.game.common.model.anno.GameMessage;
import com.game.common.util.TopicUtil;
import com.game.domain.model.DtoMessage;
import com.game.domain.model.anno.GameMessage;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * @author zheng
 */
@Service
public class SendMessageModel {
    private Logger logger = LoggerFactory.getLogger(SendMessageModel.class);
    @Autowired
    private KafkaTemplate<String, byte[]> kafkaTemplate;
    /**
     * 发送消息到kafka
     * @param data
     * @param baseTopic
     * @param key
     */
    public void sendMessageToKafka(GameMessage data, String baseTopic, Object  key){
        byte[] value = DtoMessage.serializeData(data);// 向消息总线服务发布客户端请求消息。

        String topic = TopicUtil.generateTopic(baseTopic,data.getHeader().getTraceId());
        ProducerRecord<String, byte[]> send = new ProducerRecord<String, byte[]>(topic, String.valueOf(key), value);
        kafkaTemplate.send(send);
        logger.info("send message to topic "+topic+ " successful");
    }

    /**
     *
     * @param data
     */
    public void sendMessageToKafkaSimple(GameMessage data){
        sendMessageToKafka(data, InfoConstant.GATEWAY_LOGIC_TOPIC,data.getHeader().getAttribute());
    }
}
