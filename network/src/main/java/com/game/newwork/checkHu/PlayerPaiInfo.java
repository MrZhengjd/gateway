package com.game.newwork.checkHu;

import com.game.common.relation.organ.Organ;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zheng
 */
public class PlayerPaiInfo implements Organ {

    private List<PaiInfo> innerPaiInfos = new ArrayList<>();
    private List<PaiInfo> outtedPaiInfos = new ArrayList<>();
    private List<List<PaiInfo>> outterPaiInfos = new ArrayList<>();
    private PaiInfo moPai;

    public PaiInfo getMoPai() {
        return moPai;
    }

    public void setMoPai(PaiInfo moPai) {
        this.moPai = moPai;
    }

    public List<PaiInfo> getInnerPaiInfos() {
        return innerPaiInfos;
    }
    public void removeInnerPaiId(Integer paiId){
        for (PaiInfo paiInfo : innerPaiInfos){
            if (paiInfo != null && paiInfo.getPai().getPaiId() == paiId){
                innerPaiInfos.remove(paiInfo);
                return;
            }
        }
    }

    public void setInnerPaiInfos(List<PaiInfo> innerPaiInfos) {
        this.innerPaiInfos = innerPaiInfos;
    }

    public List<PaiInfo> getOuttedPaiInfos() {
        return outtedPaiInfos;
    }

    public void setOuttedPaiInfos(List<PaiInfo> outtedPaiInfos) {
        this.outtedPaiInfos = outtedPaiInfos;
    }

    public List<List<PaiInfo>> getOutterPaiInfos() {
        return outterPaiInfos;
    }

    public void setOutterPaiInfos(List<List<PaiInfo>> outterPaiInfos) {
        this.outterPaiInfos = outterPaiInfos;
    }

    @Override
    public void reset() {
        innerPaiInfos.clear();
        outtedPaiInfos.clear();
        outterPaiInfos.clear();
    }
}
