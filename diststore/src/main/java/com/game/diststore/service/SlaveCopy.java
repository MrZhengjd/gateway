package com.game.diststore.service;

import com.game.common.cache.FastChannelInfo;
import com.game.common.cache.MasterInfo;
import com.game.common.cache.Operation;
import com.game.common.cache.ReadWriteLockOperate;
import com.game.common.concurrent.IGameEventExecutorGroup;
import com.game.common.concurrent.NonResultLocalRunner;
import com.game.common.concurrent.PromiseUtil;
import com.game.common.constant.InfoConstant;
import com.game.common.constant.RequestMessageType;
import com.game.common.eventcommand.IEvent;
import com.game.common.eventcommand.LockQueueMediator;
import com.game.common.eventdispatch.DynamicRegisterGameService;
import com.game.common.model.*;
import com.game.diststore.client.ConnectTools;
import com.game.diststore.store.UnLockQueueOneInstance;
import com.game.diststore.util.BackupUtil;
import io.netty.util.concurrent.DefaultEventExecutor;
import io.netty.util.concurrent.EventExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sun.misc.Contended;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author zheng
 */
@Component
public class SlaveCopy {
    private static Logger logger = LoggerFactory.getLogger(SlaveCopy.class);
    @Contended
    private volatile boolean start;
    @Contended
    private volatile Long lastCopyId = 1l;
    private EventExecutor eventExecutor = new DefaultEventExecutor();
    private EventExecutor eventExecutor1 = new DefaultEventExecutor();
    @Autowired
    private EventHolder eventHolder;
    protected LockQueueMediator mediator = UnLockQueueOneInstance.getInstance().getMediator();
    private ReadWriteLockOperate readWriteLockOperate = new ReadWriteLockOperate();
    @Autowired
    private DynamicRegisterGameService dynamicRegisterGameService;
    @Autowired
    private ConnectTools connectTools;
    @Autowired
    private BackupUtil backupUtil;
    @Contended
    private volatile boolean rollBack;

    public EventExecutor getEventExecutor() {
        return eventExecutor;
    }

    public EventExecutor getEventExecutor1() {
        return eventExecutor1;
    }

    public void rollback(){
        mediator.rollback();
        this.lastCopyId = mediator.getLatestOperateId();
        System.out.println("finish roll back");
    }
    public void startCopyData(){
        synchronized (InfoConstant.SLAVE_COPY.intern()){
            if (!start){
                start = true;
                eventExecutor.scheduleWithFixedDelay(new Runnable() {
                    @Override
                    public void run() {
//                        backupCopy();
                        if (!mediator.getFinishrollback()){
                            return;
                        }
                        rotateMaterCopy(MasterInfo.needCopy(mediator.getLatestOperateId()));
                    }
                },0l,200l, TimeUnit.MILLISECONDS);
                eventExecutor1.scheduleWithFixedDelay(new Runnable() {
                    @Override
                    public void run() {
//                        askForMaster();
                        askMasterChangeInfo();
                    }
                },0, 1, TimeUnit.SECONDS);

            }
        }

    }

    /**
     * 发送请求获取最新的master信息
     */
    private void askForMaster() {
        GameMessage gameMessage = dynamicRegisterGameService.getRequestInstanceByMessageId(RequestMessageType.ASK_MASTER);
        gameMessage.getHeader().setPlayerId(MasterInfo.getPlayerId());
        gameMessage.getHeader().setAttribute(InfoConstant.ASK_ROR_MASTER);
        connectTools.writeToRecorder(gameMessage);

    }

    /**
     * 复制消息
     */
    private void backupCopy() {

        if (MasterInfo.checkIsMasterOrRecorder()){
            return;
        }
        rotateCopy();


    }
    public void countDown(IEvent iEvent){
        String key = InfoConstant.BACKUP_COPY+lastCopyId;
        CountDownLatch countDownLatch = backupUtil.getCountDownLatch(key);
        if (countDownLatch != null){

            this.lastCopyId = iEvent.getEventId();

            countDownLatch.countDown();

        }
    }

    /**
     * 请求获得master改变记录
     */
    private void askMasterChangeInfo(){
        if (MasterInfo.getBandInfo() != null){
            GameMessage askMaterChange = dynamicRegisterGameService.getDefaultRequest(RequestMessageType.ASK_MASTER_CHANGE);
            askMaterChange.setMessageData(lastCopyId);
            connectTools.writeMessage(InfoConstant.LOCAL_HOST,InfoConstant.RECORDER_PORT,askMaterChange);
        }

    }

    /**
     * 和masterchange 对比过后再去复制更新
     */
    private void compareAndCopy(){
        MasterCopyInfo copyInfo = MasterInfo.needCopy(lastCopyId);
        if (copyInfo == null) {
            return;
        }

//        MasterInfo.clearMasterChange();

    }
    /**
     * 一条一条的复制
     * 如何保证顺序性 这里是一条接着一条的
     */
    private void rotateMaterCopy(MasterCopyInfo copyInfo){
        try {
            if (!mediator.getFinishrollback()){
                return;
            }
            if (copyInfo == null){
//            logger.info("copy info is null====================");
                return;
            }
//            if (MasterInfo.checkIsMasterOrRecorder() ){
//                return;
//            }
            String nodeInfo = copyInfo.getHostName();
            String host = nodeInfo.substring(0,nodeInfo.indexOf("-"));
//            if (FastChannelInfo.getChannelInfo().equals(nodeInfo)){
//                return;
//            }
            Integer port = Integer.valueOf(nodeInfo.substring(nodeInfo.indexOf("-")+1));
//        NodeInfo masterInfo = MasterInfo.getMasterInfo();
            if (nodeInfo!= null && (lastCopyId == null ||lastCopyId < copyInfo.getEndCopyId())){
                if (backupUtil.getByKey(InfoConstant.BACK_UP_COPY+lastCopyId)!= null){
                    return;
                }
                CountDownLatch latch = new CountDownLatch(1);
                backupUtil.saveLatchInfo(InfoConstant.BACKUP_COPY+lastCopyId,latch);
                CopyMessageRequest copyMessage = (CopyMessageRequest) dynamicRegisterGameService.getRequestInstanceByMessageId(RequestMessageType.COPY_DATA);
                logger.info("copy id "+lastCopyId);
                copyMessage.setMessageData(lastCopyId);
//                copyMessage.setLastCopyId(lastCopyId);
                copyMessage.getHeader().setAttribute(InfoConstant.BACK_UP_COPY);
                connectTools.writeMessage(host,port,copyMessage);

                try {
                    latch.await(10,TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return;
                }
//                lastCopyId = mediator.getLatestOperateId();
//                System.out.println("finish copy data once -----------");
                if (lastCopyId <copyInfo.getEndCopyId()){
                    System.out.println("rotate -------");
                    rotateMaterCopy(copyInfo);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    /**
     * 一条一条的复制
     * 如何保证顺序性 这里是一条接着一条的
     */
    private void rotateCopy(){
        NodeInfo masterInfo = MasterInfo.getMasterInfo();
        if (masterInfo!= null && (lastCopyId == null ||lastCopyId < masterInfo.getLatestOperateId())){
            CopyMessageRequest copyMessage = (CopyMessageRequest) dynamicRegisterGameService.getRequestInstanceByMessageId(RequestMessageType.COPY_DATA);
            copyMessage.setMessageData(lastCopyId);
            copyMessage.getHeader().setAttribute(InfoConstant.BACK_UP_COPY);
            connectTools.writeMessage(masterInfo.getHost(),masterInfo.getPort(),copyMessage);
            CountDownLatch latch = new CountDownLatch(1);
            backupUtil.saveLatchInfo(InfoConstant.BACKUP_COPY+lastCopyId,latch);
            try {
                latch.await(10,TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
                return;
            }
            lastCopyId = mediator.getLatestOperateId();
            System.out.println("finish copy data once -----------");
            if (lastCopyId < masterInfo.getLatestOperateId()){
                rotateCopy();
            }
        }
    }
}