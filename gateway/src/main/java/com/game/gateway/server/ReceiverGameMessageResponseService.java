package com.game.gateway.server;

import com.game.common.concurrent.NonResultLocalRunner;
import com.game.common.concurrent.PromiseUtil;
import com.game.common.constant.InfoConstant;
import com.game.gateway.server.sendway.SendToPlayerProxy;
import com.game.newwork.cache.ChannleMap;
import com.game.common.model.anno.GameMessage;
import com.game.gateway.model.DtoMessage;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ReceiverGameMessageResponseService {
    @Resource
    private ChannleMap channleMap;
	private Logger logger = LoggerFactory.getLogger(ReceiverGameMessageResponseService.class);
//	@Autowired
//	private GatewayServerConfig gatewayServerConfig;
//    @Autowired
//    private ChannelService channelService;
    @Autowired
    private SendToPlayerProxy sendToPlayerProxy;
    @KafkaListener(topics = InfoConstant.GATEWAY_LOGIC_TOPIC+"-"+"${game.waitstart.serverId}", groupId = "test")
    public void receiver(ConsumerRecord<String, byte[]> record, Acknowledgment acknowledgment) {
//        logger.info("here is recie data ");
        GameMessage message = DtoMessage.readMessageHeader(record.value());


        if (message == null ||message.getHeader() == null ){
            logger.error("receive data error"+message);
            acknowledgment.acknowledge();
            return;

        }
        Object key = message.getHeader().getPlayerId();
        if (message.getHeader().getAttribute() != null){
            key = message.getHeader().getAttribute();
        }
        PromiseUtil.safeExecuteNonResultWithoutExecutor(key, new NonResultLocalRunner() {
            @Override
            public void task() {
                sendToPlayerProxy.sendMessage(message.getHeader().getSendWay(),message,channleMap);
                acknowledgment.acknowledge();
            }
        });

//
    }
}
