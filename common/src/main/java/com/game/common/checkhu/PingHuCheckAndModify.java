package com.game.common.checkhu;

import com.game.common.constant.InfoConstant;
import com.game.common.model.vo.CheckHuVo;
import com.game.common.relation.RuleInfo;
import com.game.common.relation.organ.Organ;
import com.game.common.relation.pai.Pai;
import com.game.common.relation.role.PlayerRole;
import org.springframework.stereotype.Service;

/**
 * @author zheng
 */
@Service
public class PingHuCheckAndModify implements CheckAndModify {
    @Override
    public void executeCheckHu(PlayerRole playerRole, Pai pai, RuleInfo ruleInfo, CheckHuVo checkHuVo) {
        Organ organ = playerRole.getOrganMap().get(InfoConstant.HU_PAI_TYPE_MAP);

    }
}
