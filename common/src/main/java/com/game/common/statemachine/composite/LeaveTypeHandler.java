package com.game.common.statemachine.composite;

import com.game.common.statemachine.LeavePermitType;

/**
 * @author zheng
 */
public interface LeaveTypeHandler {
    Rank handleLeaveType(IStatus iStatus, LeaveRequest leaveRequest);
}
