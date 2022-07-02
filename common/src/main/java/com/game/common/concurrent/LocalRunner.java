package com.game.common.concurrent;

import io.netty.util.concurrent.Promise;

/**
 * @author zheng
 */
public interface LocalRunner<T> {
//    void task(Promise<?> promise);
    void task(Promise<?> promise,T object);
}
