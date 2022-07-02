package com.game.common.eventcommand;

import com.game.common.concurrent.LocalRunner;
import com.game.common.concurrent.PromiseUtil;
import com.game.common.generator.IdGenerator;
import com.game.common.generator.IdGeneratorFactory;
import com.game.common.serialize.DataSerialize;
import com.game.common.serialize.DataSerializeFactory;
import com.game.common.store.TempResult;
import com.game.common.walstore.UnLockWALQueue;
import io.netty.util.concurrent.Promise;
import lombok.extern.log4j.Log4j2;

/**
 * @author zheng
 */
@Log4j2
public class UnLockQueueMediatorImpl extends LockQueueMediator {
    IdGenerator idGenerator = IdGeneratorFactory.getDefaultGenerator();
    private DataSerialize dataSerialize = DataSerializeFactory.getInstance().getDefaultDataSerialize();

    public UnLockQueueMediatorImpl( UnLockWALQueue unLockQueue) {
        super(unLockQueue);
    }
//    public static CountDownLatch latch = new CountDownLatch(4);

    @Override
    public void execute(IEvent event) {
        if (rollback){
            throw new RuntimeException("here is on rollback please wait");
        }
        PromiseUtil.safeExecute(unLockQueue.getExecutor(), new LocalRunner() {
            @Override
            public void task(Promise promise, Object object) {
                if (rollback){
                    promise.setFailure(new RuntimeException("here is rollback please wait--------"));
                    return;
                }
                event.setData(new BaseData(event.getEventId(),"test-"+event.getEventId()));
                event.setEventId(idGenerator.generateIdFromServerId(event.getCalledId()));
                unLockQueue.offerData(dataSerialize.serialize(event));
                holdMap.put(event.getEventId(),event);
//                log.info("here is write message succes "+unLockQueue.getExecutor().hashCode() );
                promise.setSuccess(true);
            }
        } ,null);

    }

    // 循环取出数据，直到取出的数据为空

    private void rotatePoll(){

        try {
            long lastEventId = -99l;
            boolean needContinued = false;
            int lastServerId = -10;
            TempResult tempResult = unLockQueue.poll2();
            if (tempResult.getState() == 2){
                byte[] datas = tempResult.getDatas();
                if (datas == null){
                    return;
                }
                IEvent desEvent = null;
                try {
                    desEvent = dataSerialize.deserialize(datas, IEvent.class);
                    int temId = lastServerId;
                    long temEventId = lastEventId;
                    if (desEvent.getCalledId() != temId){
                        lastEventId = desEvent.getEventId();
                    }
                    if (desEvent.getEventId() != temEventId){
                        temEventId = desEvent.getEventId();
                    }
                    if (desEvent.getCalledId() < temId || desEvent.getEventId() < temEventId){
                        rotatePoll();
                        return;
                    }


                }catch (Exception e){
                    e.printStackTrace();
                    log.error("here is log end "+tempResult);
                }
                if (desEvent == null){
                    return;
                }
                if (desEvent.getType() != EventType.DELETE.getType()){
                    holdMap.put(desEvent.getEventId(),desEvent);
//                    log.info("remove event "+desEvent);
                }else {
                    holdMap.remove(desEvent.getEventId(),desEvent);
//                    log.info("add event "+desEvent);
                }

                rotatePoll();

            }

        } catch (Exception e){
            e.printStackTrace();
        }

    }
    @Override
    public void rollback() {
//        System.out.println("there show hashcode -- "+this.getUnLockQueue().getExecutor().hashCode());
        PromiseUtil.safeExecute(unLockQueue.getExecutor(), new LocalRunner() {
            @Override
            public void task(Promise promise, Object object) {
                if (!rollback){
                    rotatePoll();
//                    System.out.println("here use executor "+unLockQueue.getExecutor().hashCode());
                    rollback = true;
                    promise.setSuccess(true);
                }
            }
        },null);




//

    }
}
