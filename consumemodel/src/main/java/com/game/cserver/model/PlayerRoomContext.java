package com.game.cserver.model;

import com.game.common.relation.role.PlayerRole;
import com.game.common.relation.room.RoomManager;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author zheng
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PlayerRoomContext {
    private PlayerRole playerRole;
    private RoomManager roomManager;
}
