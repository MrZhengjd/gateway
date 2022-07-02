package com.game.common.generator;

public interface IdGenerator {

    Long generateId();
    Long generateIdFromServerId(Integer serverId);
}
