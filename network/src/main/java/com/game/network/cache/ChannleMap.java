package com.game.network.cache;


import com.game.common.cache.Operation;
import com.game.common.cache.ReadWriteLockOperate;
import com.game.common.cache.ReturnOperate;
//import com.game.common.model.vo.PlayerChannel;
import com.game.common.model.PlayerChannel;
import io.netty.channel.Channel;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zheng
 */
@Service
public class ChannleMap {
    private Map<Long, PlayerChannel> playerChannelMap = new HashMap<>();
    private Map<String ,PlayerChannel> channelMap = new HashMap<>();
    ReadWriteLockOperate lockUtil = new ReadWriteLockOperate();
    private void readLock(Operation operation){
        ReadWriteLockOperate lockUtil = new ReadWriteLockOperate();
        lockUtil.readLockOperation(operation);
    }

    public PlayerChannel getByPlayerId(Long playerId){

        return lockUtil.readLockReturnOperation( new ReturnOperate<PlayerChannel>() {
            @Override
            public PlayerChannel operate() {
                return playerChannelMap.get(playerId);
            }
        });
    }
    public PlayerChannel getByChannel(Channel channel){
        return lockUtil.writeLockReturnOperation(new ReturnOperate<PlayerChannel>() {
            @Override
            public PlayerChannel operate() {
                return channelMap.get(channel.id().asShortText());
            }
        });
    }
    public void addChannel(Long playerId,Channel channel){
        writeLock(new Operation() {
            @Override
            public void operate() {
                PlayerChannel playerChannel = new PlayerChannel();
                playerChannel.setPlayerId(playerId);
                playerChannel.setChannel(channel);
                playerChannelMap.put(playerId,playerChannel);
                channelMap.put(channel.id().asShortText(),playerChannel);
            }
        });
    }
    public void removeChannle(Long playerId){
        writeLock(new Operation() {
            @Override
            public void operate() {
                playerChannelMap.remove(playerId);
            }
        });
    }
    private void writeLock(Operation operation){
        ReadWriteLockOperate lockUtil = new ReadWriteLockOperate();
        lockUtil.writeLockOperation(operation);

    }
}
