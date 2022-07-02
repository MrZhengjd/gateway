package com.game.common.relation;

import com.game.common.relation.room.Room;
import com.game.common.relation.room.RoomManager;

/**
 * @author zheng
 */
public class Test {
    public static void main(String[] args) {
        Room room = new Room();
        RoomManager roomManager = new RoomManager(room);
        roomManager.initPlayingIndex();
        roomManager.changePlayingIndex();
        System.out.println(roomManager.getPlayingIndex());
    }
}
