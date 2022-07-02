package com.game.common.flow.register;



import com.game.common.flow.model.Flow;

import java.util.Map;

public interface FlowDefintionRegistry {
    Map<String, Flow> registry()throws Exception;
}
