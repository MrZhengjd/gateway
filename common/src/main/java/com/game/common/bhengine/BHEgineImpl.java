package com.game.common.bhengine;


import java.util.Map;

/**
 * @author zheng
 */
public class BHEgineImpl implements BHEngine {


    @Override
    public void handleRequest(Map<String, Object> request, RootTree rootTree) {
       rootTree.handle(request);
    }
}
