package com.game.gateway.server;


import com.alibaba.nacos.api.config.annotation.NacosValue;
import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySource;
import com.game.common.eventdispatch.DynamicRegisterGameService;
import com.game.domain.consume.SendMessageModel;
import com.game.domain.playerinstance.PlayerInstance;
import com.game.domain.playerinstance.PlayerInstanceModel;
import com.game.domain.registerservice.RegisterService;
import com.game.domain.repository.playerserver.PlayerServerRepository;
import com.game.domain.repository.token.TokenRepository;
import com.game.gateway.config.GateWayConfig;
import com.game.gateway.server.handler.ConfirmHandler;
import com.game.network.cache.ChannleMap;

import com.game.network.coder.TMessageDecoderPro;
import com.game.network.coder.TMessageEncoderPro;
import com.game.common.concurrent.IGameEventExecutorGroup;

import com.game.network.server.NettyServer;
import com.game.network.server.handler.HeartbeatHandler;

import com.game.gateway.server.handler.TGameDispatchHandler;
import com.google.common.util.concurrent.RateLimiter;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.timeout.IdleStateHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@NacosPropertySource(dataId = "example",autoRefreshed = true)
public class GameServerBoot {

    @Resource
    private ChannleMap channleMap;

//    @Resource
//    private JsonRedisManager jsonRedisManager;

    @Autowired
    private PlayerServerRepository playerServerRepository;
    @Autowired
    private PlayerInstance playerInstance;

    @Autowired
    private PlayerInstanceModel playerInstanceModel;

    @Autowired
    private TokenRepository tokenRepository;
    @Autowired
    private SendMessageModel sendMessageModel;

    @Autowired
    private RegisterService registerService;
//    @Resource
//    private PlayerInstanceService playerInstanceService;

    @Resource
    private DynamicRegisterGameService dynamicRegisterGameService;

//    @Autowired
//    private KafkaTemplate<String, byte[]> kafkaTemplate;

    @Autowired
    private GateWayConfig gateWayConfig;

    @NacosValue(value = "${useLocalCache:false}",autoRefreshed = true)
    private boolean useLocalCache;
    //    @Autowired
//    private ScheduleBean scheduleBean;
//    @PostConstruct
//    public void initRedis(){
//
//    }
    private RateLimiter globalRateLimiter;
    @Resource
    private NettyServer nettyServer;

    private void startServer(String host, int port, ChannelInitializer initializer) {
        globalRateLimiter = RateLimiter.create(3000);
        nettyServer.serverBootstrap(initializer);
        nettyServer.start(host, port);
    }

    public void startServer(String host, int port,String serviceName) {
        globalRateLimiter = RateLimiter.create(30000);
        ChannelInitializer initializer = new ChannelInitializer() {
            @Override
            protected void initChannel(Channel channel) throws Exception {
                channel.pipeline().addLast("encoder", new TMessageEncoderPro())
                        .addLast("decoder", new TMessageDecoderPro())
                        .addLast("confirmHandler",new ConfirmHandler(channleMap,playerServerRepository,playerInstanceModel,tokenRepository))
//                        .addLast("request limit",new RequestRateLimiterHandler(globalRateLimiter,100000))
                        .addLast(new IdleStateHandler(30, 12, 45))
                        .addLast(IGameEventExecutorGroup.getInstance(), new TGameDispatchHandler(channleMap,dynamicRegisterGameService,playerInstanceModel,sendMessageModel,gateWayConfig))
                ;
//                        .addLast("heartbeart handler",new HeartbeatHandler());

            }
        };
        nettyServer.setServerBoot(nettyServer.serverBootstrap(initializer));
        nettyServer.start(host, port);
        registerService.registerServiceModule(gateWayConfig.getHost(),gateWayConfig.getPort(),gateWayConfig.getName(),gateWayConfig.getClusterName(),gateWayConfig.getModuleId(),gateWayConfig.getServerId());
    }
    private ChannelInitializer initializer(){
//        TGameDispatchHandler gameDispatchHandler = new TGameDispatchHandler(jsonRedisManager, channleMap, playerRoomService,dynamicRegisterGameService,playerInstanceService,kafkaTemplate);
        ChannelInitializer initializer = new ChannelInitializer() {
            @Override
            protected void initChannel(Channel channel) throws Exception {
                channel.pipeline().addLast("encoder", new TMessageEncoderPro())
                        .addLast("decoder", new TMessageDecoderPro())
//                        .addLast("confirmHandler",new ConfirmHandler(channleMap,jsonRedisManager))
//                        .addLast("request limit",new RequestRateLimiterHandler(globalRateLimiter,100000))

                        .addLast(new IdleStateHandler(30, 12, 45))
                        .addLast("heartbeart handler",new HeartbeatHandler())
                        .addLast(IGameEventExecutorGroup.getInstance(), new TGameDispatchHandler( channleMap,dynamicRegisterGameService,playerInstanceModel,sendMessageModel,gateWayConfig))
                ;
//

            }
        };
        return initializer;
    }

    public void startServerMulti(String host, int port,String serviceName) {
        globalRateLimiter = RateLimiter.create(30000);
        ChannelInitializer initializer = initializer();
        nettyServer.setServerBoot(nettyServer.serverBootstrap(initializer));
        nettyServer.multiStart(host, port,100);
//        regist(host,port, gateWayConfig.getName(), serviceName);
//        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
//            System.out.println("connections: " + TGameDispatchHandler.nConnection.get() + " handle count "+TGameDispatchHandler.handleCount.get());
//
//        }, 0, 2, TimeUnit.SECONDS);
    }
}
