package com.game.common.relation;

import com.game.common.flow.handler.FlowHandler;
import com.game.common.flow.handler.PlayerRequestHandler;
import com.game.common.flow.model.FastContextHolder;
import com.game.common.flow.model.FlowMap;
import com.game.common.flow.model.GameFlow;
import com.game.common.flow.model.Response;
import com.game.common.flow.register.FlowMapDefinitonFactory;
import com.game.common.model.PlayerRequest;
import com.game.common.model.msg.Message;
import com.game.common.model.msg.ResponseVo;

/**
 * @author zheng
 */
public class CoreEngine {
    private PlayerRequest playerRequest;

    private Message message;

    public CoreEngine(Message message) {
        this.message = message;
    }

    public CoreEngine(PlayerRequest playerRequest) {
        this.playerRequest = playerRequest;
    }

    public void process(){
        String gameType = String.valueOf(playerRequest.getRoom().getGameType());
//        gameType = gameType == null ? "32":gameType;
        FlowMap flowMap = FlowMapDefinitonFactory.getInstance().getFlowByteId(gameType);
//        String operate;
        if (flowMap != null){
            GameFlow gameFlow = flowMap.getFlowMap().get(playerRequest.getRequestType());
            if (gameFlow == null){
                FastContextHolder.getRuntimeContext().setResponse(Response.buildResponse(ResponseVo.fail("not protocol")));
                return;
            }
            FlowHandler flowHandler = new PlayerRequestHandler();
            flowHandler.handlePlayerRequest(playerRequest, gameFlow);
        }else {
            FastContextHolder.getRuntimeContext().setResponse(Response.buildResponse(ResponseVo.fail("not such action")));
        }
//        FlowHandler
    }
}
