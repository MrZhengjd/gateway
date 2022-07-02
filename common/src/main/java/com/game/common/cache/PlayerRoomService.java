package com.game.common.cache;

import com.game.common.relation.vo.RoomServerVo;

/**
 * @author zheng
 */
public interface PlayerRoomService {
    RoomServerVo getByPlayerId(Long playerId);

    void putInfo(Long playerId, RoomServerVo roomServerVo);
}
