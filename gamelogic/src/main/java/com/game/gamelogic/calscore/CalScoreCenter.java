package com.game.gamelogic.calscore;

import com.game.common.constant.InfoConstant;
import com.game.cserver.consume.ConsumerModel;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

/**
 * @author zheng
 */
@Component
public class CalScoreCenter {
    @Autowired
    private ConsumerModel model;
    @KafkaListener(topics = {"${game.calscore.name}" }, groupId = "test" )
    public void receiver(ConsumerRecord<String, byte[]> record, Acknowledgment acknowledgment) {
        model.consumeMessageToDispatch(record, InfoConstant.GATEWAY_LOGIC_TOPIC,acknowledgment);
    }
}
