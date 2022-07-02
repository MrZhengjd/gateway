package com.game.common.relation.command;

import com.game.common.relation.organ.Organ;
import com.game.common.relation.role.BaseRole;

/**
 * @author zheng
 */
public interface Command<T extends Organ,V> {
    void execute(T organ, V data);
}
