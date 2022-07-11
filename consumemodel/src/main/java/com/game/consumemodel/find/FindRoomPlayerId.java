package com.game.consumemodel.find;

import com.game.common.annotation.BandKey;
import com.game.common.constant.InfoConstant;
import com.game.common.redis.JsonRedisManager;
import com.game.common.serialize.DataSerialize;
import com.game.common.serialize.DataSerializeFactory;
import com.game.domain.find.FindPlayerId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zheng
 */
@Service
@BandKey(key = InfoConstant.ONY_BY_MANY)
public class FindRoomPlayerId implements FindPlayerId {
    @Autowired
    private JsonRedisManager jsonRedisManager;

    private DataSerialize dataSerialize = DataSerializeFactory.getInstance().getDefaultDataSerialize();
    @Override
    public List<Long> findPlayerIds(Object key) {
        byte[] data = jsonRedisManager.getObjectHash1(InfoConstant.ROOM_PLAYER_ID, key.toString());

        return dataSerialize.deserialize(data,List.class);
    }
}
