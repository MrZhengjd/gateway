package com.game.cserver.model;

import com.game.common.relation.organ.Organ;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zheng
 */
public class OperateOrgan implements Organ {
    private Map<String,Boolean> operateMap = new HashMap<>();
    @Override
    public void reset() {
        operateMap.clear();
    }
}
