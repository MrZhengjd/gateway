package com.game.common.bhengine;


import java.util.HashMap;
import java.util.Map;

/**
 * @author zheng
 */
public class TestBHEngine {
    public static void main(String[] args) {
        BehaviorTreeBuilder builder = new BehaviorTreeBuilder();
        builder.addBehavior(new DefaultCompositeTree(SelectorEnum.Sequence,false))
                .addAndTurnTo(new DefaultCompositeTree(2,20,"age",true))
                .addBehavior(new DefaultCompositeTree(2,10000,"inback",true))
                .back()
                .addBehavior(new DefaultCompositeTree(2,40,"input",true));
        Map<String,Object> request = new HashMap<>();
        request.put("inback",8000);
        request.put("age",30);
        request.put("input",50);
        EStatus eStatus = builder.rootTree().handleRequest(request);
        System.out.println("result "+eStatus);

    }
}
