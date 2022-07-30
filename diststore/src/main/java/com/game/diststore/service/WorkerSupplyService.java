package com.game.diststore.service;


import com.game.common.cache.FastChannelInfo;
import com.game.common.cache.MasterInfo;
import com.game.common.concurrent.NonResultLocalRunner;
import com.game.common.concurrent.PromiseUtil;
import com.game.common.constant.InfoConstant;
import com.game.common.constant.RequestMessageType;
import com.game.common.eventcommand.IEvent;
import com.game.common.eventcommand.LockQueueMediator;
import com.game.common.eventdispatch.DynamicRegisterGameService;
import com.game.common.model.*;
import com.game.common.model.vo.MasterChangeVo;
import com.game.common.serialize.DataSerialize;
import com.game.common.serialize.DataSerializeFactory;
import com.game.diststore.client.ConnectTools;
import com.game.diststore.client.SendMessageSerialize;
import com.game.diststore.store.UnLockQueueOneInstance;
import com.game.diststore.util.HandleUtil;
import com.game.domain.messagedispatch.GameDispatchService;
import com.game.domain.messagedispatch.GameMessageDispatch;
import com.game.domain.messagedispatch.GameMessageListener;
import com.game.network.cache.ChannelMap;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 做为从机 也向recorder 提供服务
 * @author zheng
 */
@GameDispatchService
@Service
public class WorkerSupplyService {
    private static Logger logger = LoggerFactory.getLogger(WorkerSupplyService.class);
    @Autowired
    private ChannelMap channelMap;
    @Autowired
    private DynamicRegisterGameService dynamicRegisterGameService;
    @Autowired
    private RecordComponent recordComponent;
    @Autowired
    private ConnectTools connectTools;
    @Autowired
    private SlaveCopy slaveCopy;
    protected LockQueueMediator mediator = UnLockQueueOneInstance.getInstance().getMediator();
    private DataSerialize dataSerialize = DataSerializeFactory.getInstance().getDefaultDataSerialize();

//    @Autowired
//    private SendMessageSerialize sendMessageSerialize;
    @GameMessageListener(value = AuthMessageResponse.class)
    public void handleAuthRequest(AuthMessageResponse response){

        ResponseVo result = (ResponseVo) response.deserialzeToData();
        System.out.println("here is handle for auth "+result);
        if ( result!= null && result.getCode() == ResultStatus.SUCCESS.getValue()){
//            startMap.get(key).getLatch().countDown();
//            connectTools.getByKey((String) result.getData()).getLatch().countDown();
            SendMessageSerialize.changeInfoStatus((String)result.getData());
            SendMessageSerialize.sendMessageInSerialize((String)result.getData(),connectTools);
        }
    }
    /**
     * 获取master 最新的operateid
     * @param copyMessageRequest
     */
    @GameMessageListener(value = AskCopyInfoRequest.class)
    public void askMasterlatestOperateInfo(AskCopyInfoRequest copyMessageRequest){
        try {
            String key = (String) copyMessageRequest.deserialzeToData();
//        GameMessage response = dynamicRegisterGameService.getResponseInstanceByMessageId(copyMessageRequest.getHeader().getServiceId());
            Long latestOperateId = mediator.getLatestOperateId();

            CopyInfo copyInfo =new CopyInfo(latestOperateId, recordComponent.getCurrentNode(),FastChannelInfo.getChannelInfo(),key);
//
            HandleUtil.handleAndSendToRequestPlayer(copyMessageRequest,copyInfo,dynamicRegisterGameService,channelMap,true);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    @GameMessageListener(value = AskMasterChangeResponse.class)
    public void handleMasterChange(AskMasterChangeResponse askMasterChangeResponse){
        ResponseVo re = (ResponseVo) askMasterChangeResponse.deserialzeToData();
        if (re.getCode() != ResultStatus.SUCCESS.getValue()){
            return;
        }
        MasterChangeVo vo = (MasterChangeVo) re.getData();
        if (vo == null){
            return;
        }
        MasterInfo.clearMasterChange();
        MasterInfo.saveMasterChangeVo(InfoConstant.MASTER,vo);
    }
    @GameMessageDispatch(value = @HeaderAnno(serviceId = RequestMessageType.ASK_MASTER_CHANGE,messageType = MessageType.RPCRESPONSE))
    public void handleMasterChange1(DefaultGameMessage askMasterChangeResponse){
        ResponseVo re = (ResponseVo) askMasterChangeResponse.deserialzeToData();
        if (re.getCode() != ResultStatus.SUCCESS.getValue()){
            return;
        }
        MasterChangeVo vo = (MasterChangeVo) re.getData();
        if (vo == null){
            return;
        }
        MasterInfo.clearMasterChange();
        MasterInfo.saveMasterChangeVo(InfoConstant.MASTER,vo);
    }
    @GameMessageListener(value = CopyMessageRequest.class)
    public void copyData(CopyMessageRequest copyMessageRequest){
        Long copyId = (Long) copyMessageRequest.deserialzeToData();
        logger.info("copy message here "+copyId);
//        GameMessage response = dynamicRegisterGameService.getResponseByMessageIdSimple(copyMessageRequest.getHeader());
        mediator.getNextEvent(copyId).addListener(new GenericFutureListener<Future<? super IEvent>>() {
            @Override
            public void operationComplete(Future<? super IEvent> future) throws Exception {
                if (future.isSuccess() && future.get() != null){
                    HandleUtil.handleAndSendToRequestPlayer(copyMessageRequest,future.get(),dynamicRegisterGameService,channelMap,true);
                }else {
                    HandleUtil.handleAndSendToRequestPlayer(copyMessageRequest,"failed",dynamicRegisterGameService,channelMap,false);
                }

            }
        });


    }

    /**
     * 获得复制消息的最近消息
     * @param lastCopyId
     */
    private void takeNearestCopyInfo(Long lastCopyId){

    }

    @GameMessageListener(value = CopyMessageResponse.class)
    public void copyMasterToLocal(CopyMessageResponse copyMessageRequest){

//        GameMessage response = dynamicRegisterGameService.getResponseByMessageIdSimple(copyMessageRequest.getHeader());
        PromiseUtil.safeExecuteNonResult(slaveCopy.getEventExecutor1(), new NonResultLocalRunner() {
            @Override
            public void task() {
                ResponseVo result = (ResponseVo) copyMessageRequest.deserialzeToData();
                if (result.getData() == null){
                    return;
                }
                IEvent event = (IEvent) result.getData();
                mediator.execute(event);
                slaveCopy.countDown(event);

                System.out.println("here is handle copy message -----"+event.getEventId());
            }
        });

    }
    /**
     * 从磁盘读取数据
     * @return
     */
    private IEvent readFromDisk() {
        return null;
    }
}
