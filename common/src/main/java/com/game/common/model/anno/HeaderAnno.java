package com.game.common.model.anno;


import com.game.common.eventdispatch.Event;
import com.game.common.model.MessageType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author zheng
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface HeaderAnno {
    public int serviceId();
//    public int serverId();
//    public int
    public MessageType messageType();



}
