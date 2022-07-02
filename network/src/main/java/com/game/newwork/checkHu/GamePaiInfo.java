package com.game.newwork.checkHu;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zheng
 */
public class GamePaiInfo {
    private Map<Integer,PaiInfo> paiInfoMap = new HashMap<>();

    public Map<Integer, PaiInfo> getPaiInfoMap() {
        return paiInfoMap;
    }

    public void setPaiInfoMap(Map<Integer, PaiInfo> paiInfoMap) {
        this.paiInfoMap = paiInfoMap;
    }
}
