package com.game.common.relation.role;

import com.game.common.relation.organ.Organ;

/**
 * @author zheng
 */
public class BaseRoleManager {
    private BaseRole baseRole;

    public BaseRole getBaseRole() {
        return baseRole;
    }

    public void setBaseRole(BaseRole baseRole) {
        this.baseRole = baseRole;
    }

    public Organ getOrgan(String name){
        return baseRole.getOrganMap().get(name);
    }



}
