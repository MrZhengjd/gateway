package com.game.infrustructure.persistence.room;

import com.game.common.redis.JsonRedisManager;
import com.game.domain.repository.playerserver.PlayerServerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author zheng
 */
@Service
public class PlayerServerRepositoryImpl implements PlayerServerRepository {
    @Autowired
    private JsonRedisManager jsonRedisManager;
    @Override
    public void savePlayerServer(String key, Long playerId, Integer serverId) {
        jsonRedisManager.setObjectHash1(key,String.valueOf(playerId),serverId);
    }
}
