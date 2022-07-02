package com.game.common.checkhu;

import com.game.common.constant.InfoConstant;
import com.game.common.model.vo.CheckHuVo;
import com.game.common.relation.RuleInfo;
import com.game.common.relation.organ.InnerOrgan;
import com.game.common.relation.organ.Organ;
import com.game.common.relation.pai.Pai;
import com.game.common.relation.role.PlayerRole;
import com.game.common.relation.role.PlayerRoleManager;

/**
 * @author zheng
 */
public class PengPengHuCheckHu implements ExecuteCheckHu {
    @Override
    public void executeCheckHu(PlayerRole playerRole, Pai pai, RuleInfo ruleInfo, CheckHuVo checkHuVo) {
        PlayerRoleManager playerRoleManager = new PlayerRoleManager(playerRole);
        InnerOrgan inner = playerRoleManager.getOrganFromName(InnerOrgan.class);
        playerRoleManager.putHuPaiInfo(InfoConstant.PENG_PENG_HU,true);

    }
}
