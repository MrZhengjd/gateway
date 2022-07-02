package com.game.common.checkhu;

import com.game.common.model.vo.CheckHuVo;
import com.game.common.relation.RuleInfo;
import com.game.common.relation.pai.Pai;
import com.game.common.relation.role.PlayerRole;

/**
 * @author zheng
 */
public abstract class CheckHuDecorator implements ExecuteCheckHu {
    protected ExecuteCheckHu executeCheckHu;

    public ExecuteCheckHu getExecuteCheckHu() {
        return executeCheckHu;
    }

    public void setExecuteCheckHu(ExecuteCheckHu executeCheckHu) {
        this.executeCheckHu = executeCheckHu;
    }

    public CheckHuDecorator() {
    }

    public CheckHuDecorator(ExecuteCheckHu executeCheckHu) {
        this.executeCheckHu = executeCheckHu;
    }

    @Override
    public void executeCheckHu(PlayerRole playerRole, Pai pai, RuleInfo ruleInfo, CheckHuVo checkHuVo) {
        if (executeCheckHu != null){
            executeCheckHu.executeCheckHu(playerRole,pai,ruleInfo,checkHuVo);
        }
    }
}
