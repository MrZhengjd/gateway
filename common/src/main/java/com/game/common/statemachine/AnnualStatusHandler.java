package com.game.common.statemachine;

/**
 * @author zheng
 */
public class AnnualStatusHandler extends StatusHander {
    @Override
    protected void doHandler(LeavePermit leavePermit) {
        System.out.println("annual status handler ");
    }
}
