package com.game.common.flow.component;


import com.game.common.flow.model.Node;
import com.game.common.flow.model.Response;

public interface ExecNode {
    Response executeNode(Node node);

}
