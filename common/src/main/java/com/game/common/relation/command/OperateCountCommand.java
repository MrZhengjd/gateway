package com.game.common.relation.command;

import com.game.common.relation.organ.OperateCountOrgan;
import com.game.common.relation.organ.Organ;
import com.game.common.relation.role.BaseRole;

/**
 * @author zheng
 */
public class OperateCountCommand implements Command<OperateCountOrgan,Integer> {


    @Override
    public void execute(OperateCountOrgan organ, Integer data) {
        organ.setOperateCount(data);
    }
}
