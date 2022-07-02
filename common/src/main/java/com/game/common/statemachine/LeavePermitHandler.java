package com.game.common.statemachine;

import com.game.common.eventdispatch.EventDispatchService;
import com.game.common.eventdispatch.EventListenerAnnotation;
import com.game.common.relation.role.PlayerRole;
import com.game.common.relation.room.JoinRoomEvent;
import com.game.common.relation.room.Room;
import com.game.common.relation.room.RoomManager;
import com.game.common.statemachine.composite.LeaveRequest;
import com.game.common.statemachine.composite.LeaveRequestEngine;
import org.springframework.stereotype.Component;

/**
 * @author zheng
 */
@EventDispatchService
@Component
public class LeavePermitHandler {
    @EventListenerAnnotation(value = LeavePermit.class)
    public void process(Object object,LeavePermit leavePermit){
        Engine engine = new Engine(leavePermit);
        engine.process();
    }
    @EventListenerAnnotation(value = LeaveRequest.class)
    public void processLeaveRequest(StateEvent event,LeaveRequest leaveRequest){
        LeaveRequestEngine engine = new LeaveRequestEngine(leaveRequest);
        engine.processEvent(event);
    }

    @EventListenerAnnotation(value = JoinRoomEvent.class)
    public void joinRoom(JoinRoomEvent joinRoomEvent, RoomManager roomManager){
        roomManager.addRole(joinRoomEvent.getPlayerRole());
//        room.playerJoinRoom(joinRoomEvent.getPlayerRole());
    }
}
