package com.game.common.relation.operate;

import com.game.common.constant.InfoConstant;
import com.game.common.redis.JsonRedisManager;
import com.game.common.relation.room.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author zheng
 */
@Component
public class DefaultSaveRoomData implements SaveRoomData {
    @Autowired
    private JsonRedisManager jsonRedisManager;
    @Override
    public void saveRoomData(Room room) {
        jsonRedisManager.setObjectHash1(InfoConstant.GAME_ROOM,room.getRoomNum().toString(),room);
    }
}
