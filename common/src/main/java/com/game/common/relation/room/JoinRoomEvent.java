package com.game.common.relation.room;

import com.game.common.eventdispatch.Event;
import com.game.common.relation.role.PlayerRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author zheng
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JoinRoomEvent implements Event {
    private PlayerRole playerRole;
}
