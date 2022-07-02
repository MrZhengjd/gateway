package com.game.gateway.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author zheng
 */
@Getter
@Setter
@AllArgsConstructor
public class DtoHeader {
    private String clientIp;
    private Long playerId;

}
