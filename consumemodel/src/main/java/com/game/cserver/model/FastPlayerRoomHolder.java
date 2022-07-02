package com.game.cserver.model;

import com.game.common.relation.room.Room;
import com.game.common.relation.room.RoomManager;
import io.netty.util.concurrent.FastThreadLocal;

public class FastPlayerRoomHolder {
    private static final FastThreadLocal<PlayerRoomContext> CONTEXT_THREAD_LOCAL = new FastThreadLocal<>();


    public static PlayerRoomContext getRuntimeContext(){

        return CONTEXT_THREAD_LOCAL.get();
    }


    /**
     * 设置结果集
     * @param result
     */

    public static void setCurrentRoomManager(PlayerRoomContext context) {
        CONTEXT_THREAD_LOCAL.set(context);
    }
    public static void clear(){
        CONTEXT_THREAD_LOCAL.set(null);
        CONTEXT_THREAD_LOCAL.remove();
    }
}
