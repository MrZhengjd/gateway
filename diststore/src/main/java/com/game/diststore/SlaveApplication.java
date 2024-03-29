package com.game.diststore;

import com.game.common.cache.MasterInfo;
import com.game.common.constant.InfoConstant;
import com.game.common.constant.RequestMessageType;
import com.game.common.eventdispatch.DynamicRegisterGameService;
import com.game.common.model.BandSupplyRequest;
import com.game.common.model.vo.BandServerVo;
import com.game.network.client.ConnectTools;
import com.game.diststore.role.Master;
import com.game.diststore.role.Slave;
import com.game.diststore.service.SlaveCopy;
import com.game.diststore.store.UnLockQueueOneInstance;
import com.game.network.server.SuccessHandle;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author zheng
 */
@SpringBootApplication(scanBasePackages = "com.game")
public class SlaveApplication {
    public static void main(String[] args) {

        MasterInfo.changeInfo("slave",100235l);
        ConfigurableApplicationContext run = SpringApplication.run(RecorderApplication.class);
        Slave recorder = run.getBean(Slave.class);

        DynamicRegisterGameService dynamicRegisterGameService = run.getBean(DynamicRegisterGameService.class);
        ConnectTools connectTools = run.getBean(ConnectTools.class);
        recorder.startGame(InfoConstant.LOCAL_HOST, InfoConstant.SLAVE_PORT, new SuccessHandle() {
            @Override
            public void afterSueccess() {
//                UnLockQueueOneInstance.getInstance().getMediator().rollback();

                BandSupplyRequest gameMessage = (BandSupplyRequest) dynamicRegisterGameService.getRequestInstanceByMessageId(RequestMessageType.BAND_SERVICE);
//                gameMessage.setBandServerVo();
                gameMessage.setMessageData(new BandServerVo(InfoConstant.LOCAL_HOST,InfoConstant.MASTER_PORT));
                connectTools.writeToRecorder(gameMessage);

                SlaveCopy slaveCopy = run.getBean(SlaveCopy.class);
                slaveCopy.rollback();
                slaveCopy.startCopyData();
            }
        });

    }
}
