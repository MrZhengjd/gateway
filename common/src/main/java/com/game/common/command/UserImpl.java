package com.game.common.command;

/**
 * @author zheng
 */
public class UserImpl extends User {
    public UserImpl(ChatMediator mediator, String name) {
        super(mediator, name);
    }

    @Override
    void send(String msg) {
        mediator.sendMessage(msg,this);
    }

    @Override
    void receive(String msg) {
        System.out.println("receive message "+msg);
    }
}
