package com.game.common.concurrent;

/**
 * @author zheng
 */
public interface RollBack {
    void rollBack(Object localRunner);
}
