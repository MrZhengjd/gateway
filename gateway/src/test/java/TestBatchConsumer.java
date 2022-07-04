
import com.game.common.cache.ReadWriteLockOperate;
import com.game.common.model.DefaultGameMessage;
import com.game.common.util.JWTUtil;

import com.game.domain.model.anno.GameMessage;
import com.game.domain.model.msg.BaseChuPaiInfo;
import com.game.domain.model.msg.MessageType;
import com.game.domain.model.msg.THeader;
import com.game.gateway.model.ChuPaiMessage;
import io.netty.channel.ChannelFuture;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

//import org.junit.Test;

/**
 * @author zheng
 */
public class TestBatchConsumer {
    private static ReadWriteLockOperate readWriteLockOperate = new ReadWriteLockOperate();
//    @Test
    public static void main(String[] args) {
        try {
            testBatch();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public static void testBatch() throws InterruptedException {
        ClientFactory clientFactory = ClientFactory.getInstance();
        String host = "127.0.0.1";
        int port = 12365;
        String base = "base";
        Long basePlayerId = 10000l;
//        final Bootstrap bootstrap = clientFactory.buildNewClient();
        int startSourcePort = 1025;
        long timeMillis = System.currentTimeMillis();
        GameMessage gameMessage = new DefaultGameMessage();
        gameMessage.setHeader(new THeader(MessageType.AUTH.value));
//        gameMessage.getHeader().setModuleId(12);
        gameMessage.getHeader().setServerId(1);
        gameMessage.getHeader().setModuleId(13);
        String token = JWTUtil.getUserToken("12433",basePlayerId,basePlayerId,"1");
        gameMessage.setMessageData(token);
//        GameMessageDto gameMessageDto = GameMessageDto.buildFromGameMessge(gameMessage);
//        gameMessage.setBody("test");
//        Message request = Message.buildSendRequest("test",0);
//        for (int i = 0;i< 10;i++){
//            int temp = i % 100;
//            ChannelFuture channelFuture = clientFactory.connect(host, port+temp,null,startSourcePort );
//            startSourcePort++;
////            String token = JWTUtil.getUserToken("12433",basePlayerId+i,basePlayerId+i,1);
//
////            Message auth = Message.buildAuthRequest(token);
//            gameMessage.getHeader().setServiceId(i);
//
//            if (channelFuture != null ){
//                ChannelMap.saveChannelFuture(i,channelFuture);
////                channelFuture.channel().writeAndFlush(request).sync();
//            }
//        }
        ChannelFuture channelFuture = clientFactory.connect(host, port,null,startSourcePort );
        if (channelFuture != null ){
            channelFuture.addListener(new GenericFutureListener<Future<? super Void>>() {
                @Override
                public void operationComplete(Future<? super Void> future) throws Exception {
                    if (future.isSuccess()){
                        System.out.println("channel id "+channelFuture.channel().id());
                        channelFuture.channel().writeAndFlush(gameMessage).addListener(new GenericFutureListener<Future<? super Void>>() {
                            @Override
                            public void operationComplete(Future<? super Void> future) throws Exception {
                                if (future.isSuccess()){
                                    System.out.println("send message success");
                                    ChuPaiMessage c = new ChuPaiMessage();
                                    BaseChuPaiInfo chuPaiInfo = new BaseChuPaiInfo();
                                    chuPaiInfo.setPaiId(23);
                                    c.setHeader(new THeader(MessageType.SEND.value));
                                    c.getHeader().setModuleId(12);
                                    c.getHeader().setServiceId(10001);
                                    c.getHeader().setDescribe("323");
                                    c.setMessageData(chuPaiInfo);
                                    channelFuture.channel().writeAndFlush(c).addListener(new GenericFutureListener<Future<? super Void>>() {
                                        @Override
                                        public void operationComplete(Future<? super Void> future) throws Exception {
                                            if (future.isSuccess()){
                                                System.out.println("send second time");

                                            }
                                        }
                                    });

                                }else {
                                    System.out.println("cause "+future.cause());
                                }
                            }
                        });
                    }
                }
            });


        }
        System.out.println("finish "+(System.currentTimeMillis() - timeMillis));
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
//        Message message = null;
        CountDownLatch latch = new CountDownLatch(1);
        AtomicInteger count = new AtomicInteger(0);
        executorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                int inc = count.getAndIncrement();
                if (inc > 100){
                    executorService.shutdown();
                    latch.countDown();

                }

//                ChannelMap.rotateSendMessage(gameMessage);
            }
        },10l,10l, TimeUnit.MILLISECONDS);
        latch.await(10,TimeUnit.SECONDS);
        System.exit(1);
//        latch.await(10,TimeUnit.SECONDS);

    }

}
