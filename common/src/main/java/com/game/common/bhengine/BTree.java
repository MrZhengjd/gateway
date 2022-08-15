package com.game.common.bhengine;



import java.util.Map;

/**
 * @author zheng
 */
public interface BTree {
    EStatus handle(Map<String, Object> request);
//    CompositeBTree getBtree();
}
