package com.game.infrustructure.sendmq;

import com.game.common.constant.InfoConstant;
import com.game.common.model.GameMessage;
import com.game.common.serialize.DataSerialize;
import com.game.common.serialize.DataSerializeFactory;
import com.game.common.util.TopicUtil;
import com.game.domain.consume.SendMessageModel;
import com.game.domain.model.DtoMessage;
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
public class KafkaSendMessageModel implements SendMessageModel {
    private Logger logger = LoggerFactory.getLogger(KafkaSendMessageModel.class);
    @Autowired
    private KafkaTemplate<String, byte[]> kafkaTemplate;
    private DataSerialize dataSerialize = DataSerializeFactory.getInstance().getDefaultDataSerialize();



    @Override
    public void sendMessageToMq(GameMessage data, String baseTopic, Object key) {
        byte[] value = DtoMessage.serializeData(data);// 向消息总线服务发布客户端请求消息。
//        byte[] value = dataSerialize.serialize(data);
//        String topic = TopicUtil.generateTopic(baseTopic,data.getHeader().getTraceId());
        ProducerRecord<String, byte[]> send = new ProducerRecord<String, byte[]>(baseTopic, String.valueOf(key), value);
        kafkaTemplate.send(send);
        logger.info("send message to topic "+baseTopic+ " successful");
    }
}
