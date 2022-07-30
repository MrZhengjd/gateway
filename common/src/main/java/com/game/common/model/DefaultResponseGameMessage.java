package com.game.common.model;

/**
 * @author zheng
 */
public class DefaultResponseGameMessage extends DefaultGameMessage {
    @Override
    protected Class getBodyObjClass() {
        return ResponseVo.class;
    }
}
