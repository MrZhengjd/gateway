package com.game.common.flow.component;


import com.game.common.flow.model.Node;

public abstract class AbstractExecNode implements ExecNode {


   public void start(Node node ){

       executeNode(node);
   }
}
