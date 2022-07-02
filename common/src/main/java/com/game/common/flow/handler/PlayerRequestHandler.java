package com.game.common.flow.handler;

import com.game.common.flow.component.ExecNode;
import com.game.common.flow.component.ExecNodeFactory;
import com.game.common.flow.component.PlayerContext;
import com.game.common.flow.model.FastContextHolder;
import com.game.common.flow.model.GameFlow;
import com.game.common.flow.model.Node;
import com.game.common.flow.model.Response;
import com.game.common.model.PlayerRequest;

/**
 * @author zheng
 */
public class PlayerRequestHandler implements FlowHandler {


    @Override
    public Response handlePlayerRequest(PlayerRequest playerRequest, GameFlow gameFlow) {

//        Request request = ClassUtil.newInstance(flow.getInput(),Request.class);

        Response response = new Response();
        PlayerContext context = new PlayerContext();
        context.setPlayerRequest(playerRequest);
        context.setResponse(response);
        FastContextHolder.setCurrentContext(context);
        ExecNode parser ;
        for (Node node : gameFlow.getNodeList()){
            parser = ExecNodeFactory.getInstance().getNodeParserByType(node.getType());
            FastContextHolder.getRuntimeContext().setResponse(parser.executeNode(node));
        }
//        Node startNode = FlowMapDefinitonFactory.getInstance().getNodeMap().get(gameFlow.getStart());
//        FlowHander handed = new FlowHander();
//        return handed.handleEvent(startNode);
        return FastContextHolder.getRuntimeContext().getResponse();

    }
}
