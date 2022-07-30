package com.game.common.model;

import com.game.common.eventdispatch.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.net.InetSocketAddress;

/**
 * @author zheng
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OfflineEvent implements Event {

    private InetSocketAddress address;

}
