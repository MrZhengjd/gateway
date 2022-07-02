package com.game.gateway.server;


import com.alibaba.nacos.api.annotation.NacosInjected;
import com.alibaba.nacos.api.config.annotation.NacosValue;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.alibaba.nacos.client.naming.beat.BeatInfo;
import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySource;
import com.game.common.cache.PlayerRoomService;
import com.game.common.constant.InfoConstant;
import com.game.gateway.config.GateWayConfig;
import com.game.newwork.cache.ChannleMap;

import com.game.newwork.coder.TMessageDecoderPro;
import com.game.newwork.coder.TMessageEncoderPro;
import com.game.common.concurrent.IGameEventExecutorGroup;
import com.game.common.model.anno.DynamicRegisterGameService;

import com.game.common.redis.JsonRedisManager;
import com.game.newwork.server.NettyServer;
import com.game.newwork.server.handler.ConfirmHandler;
import com.game.newwork.server.handler.HeartbeatHandler;

import com.game.gateway.server.handler.PlayerInstanceService;
import com.game.gateway.server.handler.TGameDispatchHandler;
import com.google.common.util.concurrent.RateLimiter;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.timeout.IdleStateHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@NacosPropertySource(dataId = "example",autoRefreshed = true)
public class GameServerBoot {

    @Resource
    private ChannleMap channleMap;

    @Resource
    private JsonRedisManager jsonRedisManager;

    @Resource
    private PlayerRoomService playerRoomService;

    @Resource
    private PlayerInstanceService playerInstanceService;

    @Resource
    private DynamicRegisterGameService dynamicRegisterGameService;
    @NacosInjected
    private NamingService namingService;
    @Autowired
    private KafkaTemplate<String, byte[]> kafkaTemplate;

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
                        .addLast("confirmHandler",new ConfirmHandler(channleMap,jsonRedisManager))
//                        .addLast("request limit",new RequestRateLimiterHandler(globalRateLimiter,100000))
                        .addLast(new IdleStateHandler(30, 12, 45))
                        .addLast(IGameEventExecutorGroup.getInstance(), new TGameDispatchHandler(jsonRedisManager, channleMap, playerRoomService,dynamicRegisterGameService,playerInstanceService,kafkaTemplate,gateWayConfig))
                ;
//                        .addLast("heartbeart handler",new HeartbeatHandler());

            }
        };
        nettyServer.setServerBoot(nettyServer.serverBootstrap(initializer));
        nettyServer.start(host, port);
        regist(gateWayConfig.getHost(),gateWayConfig.getPort(),gateWayConfig.getName(),gateWayConfig.getClusterName(),gateWayConfig.getModuleId(),gateWayConfig.getServerId());
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
                        .addLast(IGameEventExecutorGroup.getInstance(), new TGameDispatchHandler(jsonRedisManager, channleMap, playerRoomService,dynamicRegisterGameService,playerInstanceService,kafkaTemplate,gateWayConfig))
                ;
//

            }
        };
        return initializer;
    }
    private void regist(String host, int port, String clusterName, String serviceName,Integer serviceId,Integer serverId){
        try {
            BeatInfo beatInfo = new BeatInfo();
            Instance instance = new Instance();
            instance.setIp(host);
            instance.setPort(port);
            instance.setClusterName(clusterName);
            instance.setServiceName(serviceName);
            instance.setEphemeral(true);
            instance.getMetadata().put(InfoConstant.MODULE_ID,String.valueOf(serviceId));
            instance.getMetadata().put(InfoConstant.SERVER_ID,String.valueOf(serverId));
            namingService.registerInstance(clusterName,instance);

            System.out.println("use cache "+useLocalCache);
        } catch (NacosException e) {
            e.printStackTrace();
        }
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
