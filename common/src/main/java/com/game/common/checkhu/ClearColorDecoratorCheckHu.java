package com.game.common.checkhu;

import com.game.common.constant.InfoConstant;
import com.game.common.model.vo.CheckHuVo;
import com.game.common.relation.RuleInfo;
import com.game.common.relation.pai.Pai;
import com.game.common.relation.role.PlayerRole;
import com.game.common.relation.role.PlayerRoleManager;

/**
 * @author zheng
 */
public class ClearColorDecoratorCheckHu extends CheckHuDecorator {
    @Override
    public void executeCheckHu(PlayerRole playerRole, Pai pai, RuleInfo ruleInfo, CheckHuVo checkHuVo) {
        PlayerRoleManager playerRoleManager = new PlayerRoleManager(playerRole);
        playerRoleManager.putHuPaiInfo(InfoConstant.CLEAR_COLOR,true);
    }
}
