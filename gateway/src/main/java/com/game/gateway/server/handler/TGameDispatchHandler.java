package com.game.gateway.server.handler;

import com.game.common.eventdispatch.DynamicRegisterGameService;
import com.game.common.util.TopicUtil;
import com.game.domain.cache.PlayerRoomService;
import com.game.domain.model.anno.GameMessage;
import com.game.gateway.config.GateWayConfig;
import com.game.domain.model.msg.RequestMessageType;
import com.game.domain.model.msg.ResponseVo;
import com.game.newwork.cache.ChannleMap;


import com.game.common.redis.JsonRedisManager;


import com.game.common.serialize.DataSerialize;
import com.game.common.serialize.DataSerializeFactory;
import com.game.common.util.JWTUtil;
import com.game.common.util.NettyUtils;
import com.game.gateway.model.DtoMessage;
import com.game.newwork.server.handler.ConfirmHandler;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.concurrent.*;
import lombok.extern.log4j.Log4j2;
//import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author zheng
 */
//@ChannelHandler.Sharable
@Log4j2
public class TGameDispatchHandler extends SimpleChannelInboundHandler<GameMessage> {
    private DataSerialize serializeUtil = DataSerializeFactory.getInstance().getDefaultDataSerialize();
    private JsonRedisManager jsonRedisManager;
    private PlayerRoomService playerRoomService;
    private DynamicRegisterGameService dynamicRegisterGameService;
    private PlayerInstanceService playerInstanceService;
    private GateWayConfig gateWayConfig;
    private KafkaTemplate<String, byte[]> kafkaTemplate;
    //jdk1.5 并发包中的用于计数的类
    public static AtomicInteger nConnection = new AtomicInteger();
    private ChannleMap channleMap;
    public static AtomicLong handleCount = new AtomicLong(0);
//    private static GameStatusHandleMap gameStatusHandleMap
    public TGameDispatchHandler(JsonRedisManager jsonRedisManager, ChannleMap channleMap) {
        this.jsonRedisManager = jsonRedisManager;
        this.channleMap = channleMap;
    }


    public TGameDispatchHandler(JsonRedisManager jsonRedisManager, ChannleMap channleMap, PlayerRoomService playerRoomService) {
        this.jsonRedisManager = jsonRedisManager;
        this.channleMap = channleMap;
        this.playerRoomService = playerRoomService;

    }
    public TGameDispatchHandler(JsonRedisManager jsonRedisManager, ChannleMap channleMap, PlayerRoomService playerRoomService,DynamicRegisterGameService dynamicRegisterGameService) {
        this.jsonRedisManager = jsonRedisManager;
        this.channleMap = channleMap;
        this.playerRoomService = playerRoomService;
        this.dynamicRegisterGameService = dynamicRegisterGameService;
    }
    public TGameDispatchHandler(JsonRedisManager jsonRedisManager, ChannleMap channleMap, PlayerRoomService playerRoomService,DynamicRegisterGameService dynamicRegisterGameService,PlayerInstanceService playerInstanceService,KafkaTemplate<String, byte[]> kafkaTemplate,GateWayConfig gateWayConfig) {
        this.jsonRedisManager = jsonRedisManager;
        this.channleMap = channleMap;
        this.playerRoomService = playerRoomService;
        this.dynamicRegisterGameService = dynamicRegisterGameService;
        this.playerInstanceService = playerInstanceService;
        this.kafkaTemplate = kafkaTemplate;
        this.gateWayConfig = gateWayConfig;
    }
    private JWTUtil.TokenBody tokenBody;
    private ResponseVo result(Map<String ,Object> message){

        ResponseVo responseVo = ResponseVo.success(message,"statusNotWright");
        return responseVo;
    }
    private void handleAndSendMessage( GameMessage gameMessage,ChannelHandlerContext ctx,Map<String ,Object> map){
        handleAndSendMessage(gameMessage,ctx,map);
    }
//    private void handleAndSendMessage( GameMessage gameMessage,ChannelHandlerContext ctx,Object result){
//        gameMessage.setBody(result);
//        ctx.writeAndFlush(gameMessage);
//    }

    /**
     * 获取玩家所在服务器后进行操作
     * @param ctx
     * @param moduleId
     * @param successHandler
     */
    private void operateAfterSelectServer(ChannelHandlerContext ctx, Integer moduleId,SuccessHandler successHandler){

        if (tokenBody == null) {// 如果首次通信，获取验证信息
            ConfirmHandler confirmHandler = (ConfirmHandler) ctx.channel().pipeline().get("confirmHandler");
            if (confirmHandler != null){
                tokenBody = confirmHandler.getTokenBody();
            }

        }
//        ctx.fireChannelRead(message);
        playerInstanceService.selectServerId(tokenBody.getPlayerId(),moduleId).addListener(new GenericFutureListener<Future<Integer>>() {
            @Override
            public void operationComplete(Future<Integer> future) throws Exception {
                if (future.isSuccess()){
                    successHandler.successHandler(future);
                }
            }
        });
    }
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GameMessage message) throws Exception {
//
        operateAfterSelectServer(ctx,message.getHeader().getModuleId(), new SuccessHandler() {
            @Override
            public void successHandler(Future<Integer> future) {
               readyAndSendMessage(future,message,ctx);
            }
        });
        ctx.fireChannelRead(message);


    }

    /**
     * 准备并转发消息
     * @param future
     * @param message
     * @param ctx
     */
    private void readyAndSendMessage(Future<Integer> future,GameMessage message,ChannelHandlerContext ctx){
        Integer toServerId = null;
        try {
            toServerId = future.get();
            message.getHeader().setPlayerId(tokenBody.getPlayerId());
            message.getHeader().setClientIp(NettyUtils.getRemoteIP(ctx.channel()));
//
            message.getHeader().setTraceId(gateWayConfig.getServerId());
            String topic = TopicUtil.generateTopic(playerInstanceService.getModuleName(message.getHeader().getModuleId()), toServerId);// 动态创建与业务服务交互的消息总线Topic
//                    String topic = "gamemessage";
            System.out.println("here is topic"+topic);
            byte[] value = DtoMessage.serializeData(message);// 向消息总线服务发布客户端请求消息。

            ProducerRecord<String, byte[]> record = new ProducerRecord<String, byte[]>(topic, String.valueOf(tokenBody.getPlayerId()), value);
            kafkaTemplate.send(record);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
//        super.exceptionCaught(ctx,cause);
        Channel channel = ctx.channel();
        if(channel.isActive()){
            nConnection.decrementAndGet();
            ctx.close();
        }
//        log.info("here is channel close "+ctx.channel().id().asShortText()+" and playerId "+(tokenBody == null ? " null ":tokenBody.getPlayerId()));
    }



    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
//        super.channelActive(ctx);
        nConnection.getAndIncrement();
        GameMessage online = dynamicRegisterGameService.getRequestInstanceByMessageId(RequestMessageType.ONLINE_MESSAGE);
        operateAfterSelectServer(ctx, -1, new SuccessHandler() {
            @Override
            public void successHandler(Future<Integer> future) {
                readyAndSendMessage(future,online,ctx);
            }
        });
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
//        super.channelInactive(ctx);
        nConnection.getAndIncrement();
        GameMessage offline = dynamicRegisterGameService.getRequestInstanceByMessageId(RequestMessageType.OFFLINE_MESSAGE);
        operateAfterSelectServer(ctx, -1, new SuccessHandler() {
            @Override
            public void successHandler(Future<Integer> future) {
                readyAndSendMessage(future,offline,ctx);
            }
        });
    }
}
