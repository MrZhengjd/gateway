package com.game.common.checkhu;

import com.game.common.model.vo.CheckHuVo;
import com.game.common.relation.RuleInfo;
import com.game.common.relation.pai.Pai;
import com.game.common.relation.role.PlayerRole;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zheng
 */
public class BaseCheckHuModel implements CheckHuModel {
    private List<ExecuteCheckHu> checkHus = new ArrayList<>();
//    private CheckHuProxy checkHuProxy;

    /**
     * 用decorator有个坏处就是不能通过反射来获得一个实体类
     * 每个玩法要新建一个流程，写好对应的类
     * @param playerRole
     * @param pai
     * @param ruleInfo
     */
    @Override
    public void checkHu(PlayerRole playerRole, Pai pai, RuleInfo ruleInfo) {
        CheckHuDecorator base = new DefaultCheckHuDecorator();
        for (ExecuteCheckHu executeCheckHu :checkHus){
            addAfterDecorator(base,playerRole,pai,ruleInfo,executeCheckHu);
        }
        CheckHuVo vo = new CheckHuVo();
        base.executeCheckHu(playerRole,pai,ruleInfo,vo);

    }

    /**
     * 再decorator里面再额外添加decorator
     * @param base
     * @param playerRole
     * @param pai
     * @param ruleInfo
     * @param executeCheckHu
     */
    private void addAfterDecorator(CheckHuDecorator base, PlayerRole playerRole, Pai pai, RuleInfo ruleInfo, ExecuteCheckHu executeCheckHu){

        DefaultCheckHuDecorator decorator = new DefaultCheckHuDecorator();
        decorator.setExecuteCheckHu(executeCheckHu);
        base.setExecuteCheckHu(decorator);
//        decorator.executeCheckHu(playerRole,pai,ruleInfo,vo);
//        return decorator;
    }
}
