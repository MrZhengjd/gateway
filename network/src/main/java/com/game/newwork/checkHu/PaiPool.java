package com.game.newwork.checkHu;


import com.game.domain.relation.pai.Pai;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zheng
 */
public class PaiPool {
    private static Map<Integer, Pai> paiPool = new HashMap<>();
    static {

    }

    public static Pai getPaiById(Integer paiId){
        if (paiPool.containsKey(paiId)){
            return paiPool.get(paiId);
        }
        Pai pai = new Pai();
        pai.setPaiId(paiId);
        pai.setPaiType(paiId / 10);
        paiPool.put(paiId,pai);
        return pai;
    }
}
