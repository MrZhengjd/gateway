package com.game.common.command;

/**
 * @author zheng
 */
public interface ChatMediator {
    void sendMessage(String msg,User user);
    void addUser(User user);
}
