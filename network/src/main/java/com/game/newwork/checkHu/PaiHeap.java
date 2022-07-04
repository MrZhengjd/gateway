package com.game.newwork.checkHu;


import com.game.domain.relation.pai.Pai;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zheng
 */
public class PaiHeap {
    private static List<Pai> paiList = new ArrayList<>();
    static {
        for (int i = 11;i< 47;i++){
            if (i % 10 == 0){
                continue;
            }
            Pai pai = new Pai();
            pai.setPaiId(i);
            paiList.add(pai);
        }
    }
}
