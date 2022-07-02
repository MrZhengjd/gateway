package com.game.common.flow.component;

import com.game.common.eventdispatch.EventAnnotationManager;
import com.game.common.flow.model.FastContextHolder;
import com.game.common.flow.model.Node;
import com.game.common.flow.model.Response;

/**
 * @author zheng
 */
public class EventExecNode implements ExecNode {
    @Override
    public Response executeNode(Node node) {
        PlayerContext runtimeContext = FastContextHolder.getRuntimeContext();
        EventAnnotationManager.getInstance().sendPlayerEvent(node.getComponent(),node,  runtimeContext.getPlayerRequest());
        return runtimeContext.getResponse();
    }
}
