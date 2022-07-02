package com.game.common.eventcommand;

/**
 * @author zheng
 */
public interface EventMediator {
    void process(IEvent iEvent,LockQueueMediator mediator);
    void rollback(byte[] data);
}
