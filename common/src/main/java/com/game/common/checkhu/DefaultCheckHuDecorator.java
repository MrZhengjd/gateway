package com.game.common.checkhu;

import com.game.common.model.vo.CheckHuVo;
import com.game.common.relation.RuleInfo;
import com.game.common.relation.pai.Pai;
import com.game.common.relation.role.PlayerRole;

/**
 * @author zheng
 */
public class DefaultCheckHuDecorator extends CheckHuDecorator {
    @Override
    public void executeCheckHu(PlayerRole playerRole, Pai pai, RuleInfo ruleInfo, CheckHuVo checkHuVo) {
        super.executeCheckHu(playerRole, pai, ruleInfo, checkHuVo);
    }
}
