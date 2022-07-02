package com.game.newwork.checkHu;

import com.game.common.relation.pai.Pai;

/**
 * @author zheng
 */
public class PaiInfo {
    private Pai pai;

    private boolean ghost;
    private int gangType;

    public int getGangType() {
        return gangType;
    }

    public void setGangType(int gangType) {
        this.gangType = gangType;
    }

    public boolean isGhost() {
        return ghost;
    }

    public void setGhost(boolean ghost) {
        this.ghost = ghost;
    }

    public Pai getPai() {
        return pai;
    }

    public void setPai(Pai pai) {
        this.pai = pai;
    }

    public PaiInfo(Pai pai,  boolean ghost, int gangType) {
        this.pai = pai;

        this.ghost = ghost;
        this.gangType = gangType;
    }


    public static PaiInfo buildByPaiId(int paiId,boolean ghost,int gangType){
        Pai pai = PaiPool.getPaiById(paiId);
        return new PaiInfo(pai,ghost,gangType);
    }

    public static PaiInfo buildByPaiId(int paiId){
        return buildByPaiId(paiId,false,0);
    }
}
