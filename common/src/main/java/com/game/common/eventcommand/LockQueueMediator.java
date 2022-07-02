package com.game.common.eventcommand;

import com.game.common.store.UnLockQueue;
import com.game.common.walstore.UnLockWALQueue;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zheng
 */
public abstract class LockQueueMediator {
    public LockQueueMediator( UnLockWALQueue unLockQueue) {
        this.holdMap = holdMap = new HashMap<>();
        this.unLockQueue = unLockQueue;
    }
    protected volatile boolean rollback = false;
    protected Map<Long,Object> holdMap ;
    protected UnLockWALQueue unLockQueue;

    public UnLockWALQueue getUnLockQueue() {
        return unLockQueue;
    }

    public void setUnLockQueue(UnLockWALQueue unLockQueue) {
        this.unLockQueue = unLockQueue;
        holdMap = new HashMap<>();
    }
    public void clear(){
        holdMap.clear();
        rollback = false;
    }
    public abstract void execute(IEvent event);
    public abstract void rollback();
}
