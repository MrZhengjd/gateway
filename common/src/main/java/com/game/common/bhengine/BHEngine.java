package com.game.common.bhengine;


import java.util.Map;

/**
 * @author zheng
 */
public interface BHEngine {
    void handleRequest(Map<String, Object> name, RootTree rootTree);
}
