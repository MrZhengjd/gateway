package com.game.common.flow.register;

import com.game.common.flow.model.FlowMap;

import java.util.Map;

/**
 * @author zheng
 */
public interface FlowMapDefintionRegistry {
    Map<String, FlowMap> registry()throws Exception;
}
