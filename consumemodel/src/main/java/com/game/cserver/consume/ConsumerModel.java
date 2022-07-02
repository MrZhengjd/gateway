package com.game.cserver.consume;

import com.game.common.concurrent.IGameEventExecutorGroup;
import com.game.common.concurrent.LocalRunner;
import com.game.common.concurrent.NonResultLocalRunner;
import com.game.common.concurrent.PromiseUtil;
import com.game.common.constant.InfoConstant;
import com.game.common.flow.model.FastContextHolder;
import com.game.common.flow.model.FastRoomHolder;
import com.game.common.messagedispatch.GameMessageDispatchService;
import com.game.common.model.DtoMessage;
import com.game.common.model.MessageSendType;
import com.game.common.model.anno.DynamicRegisterGameService;
import com.game.common.model.anno.GameMessage;
import com.game.common.model.msg.THeader;
import com.game.common.redis.JsonRedisManager;
import com.game.common.relation.CoreEngine;
import com.game.common.relation.role.PlayerRole;
import com.game.common.relation.room.Room;
import com.game.common.relation.room.RoomManager;
import com.game.common.serialize.DataSerialize;
import com.game.common.serialize.DataSerializeFactory;
import com.game.common.util.TopicUtil;
import com.game.common.walstore.UnLockWALQueueInstance;
import com.game.cserver.find.FindPlayerIdProxy;
import com.game.cserver.model.FastPlayerRoomHolder;
import com.game.cserver.model.PlayerRoomContext;
import com.game.cserver.send.SendMessageModel;
import com.game.newwork.cache.ChannleMap;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.Promise;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;

import org.slf4j.Logger;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * @author zheng
 */
@Component
public class ConsumerModel {
    @Autowired
    private SendMessageModel sendMessageModel;
    private static Logger logger = LoggerFactory.getLogger(ConsumerModel.class);
    private static IGameEventExecutorGroup gameEventExecutorGroup = IGameEventExecutorGroup.getInstance();
    @Autowired
    private DynamicRegisterGameService dynamicRegisterGameService;

    private DataSerialize dataSerialize = DataSerializeFactory.getInstance().getDefaultDataSerialize();
    @Autowired
    private JsonRedisManager jsonRedisManager;
    @Resource
    private ChannleMap channleMap;

    @Autowired
    private FindPlayerIdProxy proxy;
    @Autowired
    private KafkaTemplate<String, byte[]> kafkaTemplate;
    @Autowired
    private GameMessageDispatchService gameMessageDispatchService;
    public  void consumeMessage(ConsumerRecord<String, byte[]> record, NonResultLocalRunner localRunner){
        GameMessage message = DtoMessage.readMessageHeader(record.value(),dynamicRegisterGameService);

        PromiseUtil.safeExecuteNonResultWithoutExecutor(getKey(message),localRunner);
//        PromiseUtil.safeExecute(gameEventExecutorGroup.selectByHash(key), localRunner,message);
    }

    /**
     * 获取key
     * @param record
     * @return
     */
    private Object getKey(GameMessage message){

//        CoreEngine coreEngine = new CoreEngine();
        Object key = message.getHeader().getPlayerId();
        if (message.getHeader().getAttribute() != null){
            key = message.getHeader().getAttribute();
        }
        return key;
    }

    /**
     * 处理完消息一对一发送消息
     * @param record
     * @param localRunner
     * @param baseTopic
     */
    public void consumeModelOneByOne(ConsumerRecord<String,byte[]> record, LocalRunner<GameMessage> localRunner, String baseTopic){
        GameMessage message = DtoMessage.readMessageHeader(record.value(),dynamicRegisterGameService);
        message = dynamicRegisterGameService.getRequestInstanceByMessageId(message.getHeader().getServiceId());
//        CoreEngine coreEngine = new CoreEngine();
        oneByOneHandle(getKey(message),localRunner,message,baseTopic);

    }

    /**
     * 处理完消息后一对一发送给当前的玩家
     * @param key
     * @param localRunner
     * @param message
     * @param baseTopic
     */
    public void oneByOneHandle(final Object key,LocalRunner localRunner,GameMessage message,String baseTopic){
        PromiseUtil.safeExecuteWithKey(key,localRunner,message).addListener(new GenericFutureListener<Future<GameMessage>>() {
            @Override
            public void operationComplete(Future<GameMessage> future) throws Exception {
                if (future.isSuccess()){
                    GameMessage data = future.getNow();
                    sendMessageModel.sendMessageToKafka(data,baseTopic,key);
                }
            }
        });
    }



    /**
     * 准备消息发送给多个玩家
     * @param key
     * @param localRunner
     * @param message
     * @param baseTopic
     */
    public void oneByManyHandle(Object key,LocalRunner localRunner,GameMessage message,String baseTopic){
        PromiseUtil.safeExecuteWithKey(key,localRunner,message).addListener(new GenericFutureListener<Future<GameMessage>>() {
            @Override
            public void operationComplete(Future<GameMessage> future) throws Exception {
                if (future.isSuccess()){
                    GameMessage data = future.getNow();
                    List<Long> playerIds = proxy.findPlayerIdsByHeader(data.getHeader());
                    data.getHeader().setToPlayerIds(playerIds);
                    data.getHeader().setSendWay(MessageSendType.ONE_BY_MANY.value);
                    sendMessageModel.sendMessageToKafka(data,baseTopic,key);
                }
            }
        });
    }
    public LocalRunner test(ConsumerRecord<String, byte[]> record, DynamicRegisterGameService dynamicRegisterGameService,
                            KafkaTemplate<String, byte[]> kafkaTemplate, GameMessageDispatchService gameMessageDispatchService, GameMessage message, Object endKey){
        return new LocalRunner<GameMessage>() {
            @Override
            public  void task(Promise<?> promise, GameMessage object) {
                FastContextHolder.setResponse("data");

                try {
                    gameMessageDispatchService.sendGameMessage(object);
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
//                GameMessage result = new DefaultGameMessage();
                GameMessage result = dynamicRegisterGameService.getResponseInstanceByMessageId(object.getHeader().getServiceId());
                if (result == null){
                    logger.info("error no such data "+message.getHeader().getServiceId() +" for response");
                    return;
                }

                if (FastContextHolder.getRuntimeContext().getResponse() == null) {
                    logger.info("need response");
                    return;
                }
                result.setMessageData(FastContextHolder.getRuntimeContext().getResponse());
                byte[] value = DtoMessage.serializeData(message);// 向消息总线服务发布客户端请求消息。
                UnLockWALQueueInstance.getInstance().chooseByHash(endKey).offerData1(value);
                String topic = TopicUtil.generateTopic(InfoConstant.GATEWAY_LOGIC_TOPIC,message.getHeader().getTraceId());
                ProducerRecord<String, byte[]> send = new ProducerRecord<String, byte[]>(topic, String.valueOf(message.getHeader().getPlayerId()), value);
                kafkaTemplate.send(send);

                logger.info("here is handle data success");
            }


        };
    }


    /**
     * 处理完消息后同一个消息发送给多个玩家
     * @param record
     * @param localRunner
     * @param baseTopic
     */
    public void consumeModelOneByManySameMessage(ConsumerRecord<String,byte[]> record, LocalRunner<GameMessage> localRunner, String baseTopic){
        GameMessage message = DtoMessage.readMessageHeader(record.value(),dynamicRegisterGameService);

//        CoreEngine coreEngine = new CoreEngine();

        oneByManyHandle(getKey(message),localRunner,message,baseTopic);

    }

    /**
     * 消费消息 房间类的处理
     * @param tHeader
     */
    public void consumeMessageInRoom(THeader tHeader){
        try {
            byte[] message = jsonRedisManager.getObjectHash1(InfoConstant.GAME_ROOM, tHeader.getPlayerId().toString());
            Room room = dataSerialize.deserialize(message, Room.class);
            if (room != null){

                RoomManager roomManager = new RoomManager(room);
                PlayerRole playerRole = roomManager.getById(tHeader.getPlayerId());
                FastPlayerRoomHolder.setCurrentRoomManager(new PlayerRoomContext(playerRole,roomManager));

            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            FastPlayerRoomHolder.clear();
        }
    }
    /**
     * 消费消息并转发
     * @param record
     * @param baseTopic
     * @param acknowledgment
     */
    public void consumeMessageToDispatch(ConsumerRecord<String,byte[]> record, String baseTopic, Acknowledgment acknowledgment){
        consumMessageByWithDiffer(record, new LocalRunner<GameMessage>() {
            @Override
            public void task(Promise promise, GameMessage object) {
                try {
                    gameMessageDispatchService.sendGameMessage(object);
                    acknowledgment.acknowledge();
                    promise.setSuccess(true);
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        },baseTopic);
    }

    /**
     * 用coreEngine去处理消息
     * @param record
     * @param baseTopic
     * @param acknowledgment
     */
    public void consumerMessageWithCoreEngineToDispatch(ConsumerRecord<String,byte[]> record, String baseTopic, Acknowledgment acknowledgment){
        consumMessageByWithDiffer(record, new LocalRunner<GameMessage>() {
            @Override
            public void task(Promise promise, GameMessage object) {
                try {
                    consumeMessageInRoom(object.getHeader());

//                    gameMessageDispatchService.sendGameMessage(object);
                    acknowledgment.acknowledge();
                    promise.setSuccess(true);
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        },baseTopic);
    }
    /**
     * 处理房间类里面的消息
     * @param record
     * @param baseTopic
     * @param acknowledgment
     */
    public void consumeRoomMessageToDispatch(ConsumerRecord<String,byte[]> record, String baseTopic, Acknowledgment acknowledgment){
        consumMessageByWithDiffer(record, new LocalRunner<GameMessage>() {
            @Override
            public void task(Promise promise, GameMessage object) {
                try {
                    consumeMessageInRoom(object.getHeader());
                    gameMessageDispatchService.sendGameMessage(object);
                    acknowledgment.acknowledge();
                    promise.setSuccess(true);
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        },baseTopic);
    }

    /**
     * 消费kafka消息并绑定不同eventExecutor
     * key 是playerID 或者attribute
     * @param record
     * @param localRunner
     * @param baseTopic
     */
    public void consumMessageByWithDiffer(ConsumerRecord<String,byte[]> record, LocalRunner<GameMessage> localRunner, String baseTopic){
        GameMessage message = DtoMessage.readMessageHeader(record.value(),dynamicRegisterGameService);
        PromiseUtil.safeExecuteWithKey(getKey(message),localRunner,message);
    }
}
