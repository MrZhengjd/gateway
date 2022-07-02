package com.game.cserver.statusmachine;

import com.game.common.relation.room.Room;

/**
 * @author zheng
 */
public interface RoomStatusHandler {
   void handleStateEvent(Room room);
   void changeNextState(Room room);
}
