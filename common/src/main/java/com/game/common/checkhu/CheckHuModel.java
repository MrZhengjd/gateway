package com.game.common.checkhu;

import com.game.common.relation.RuleInfo;
import com.game.common.relation.pai.Pai;
import com.game.common.relation.role.PlayerRole;

/**
 * @author zheng
 */
public interface CheckHuModel {
    void checkHu(PlayerRole playerRole, Pai pai, RuleInfo ruleInfo);
}
