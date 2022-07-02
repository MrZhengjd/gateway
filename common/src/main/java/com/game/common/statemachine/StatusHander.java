package com.game.common.statemachine;

/**
 * @author zheng
 */
public abstract class StatusHander implements AbstractStatusHandler {
    @Override
    public void handle(LeavePermit leavePermit) {
        before(leavePermit);
        doHandler(leavePermit);
        after(leavePermit);
    }
    protected void before(LeavePermit leavePermit){}
    protected abstract void doHandler(LeavePermit leavePermit);
    protected void after(LeavePermit leavePermit){
//        EventAnnotationManager.getInstance().sendEvent(leavePermit,"test");
        goNextStatusHandler(leavePermit);
    }
    protected void goNextStatusHandler(LeavePermit leavePermit){
        StatusMachine statusMachine = new AnnualLeaveMachine();
        statusMachine.getNextStatus(leavePermit.getStatus(),leavePermit.getEvent());
    }
}
