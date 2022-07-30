package com.game.common.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author zheng
 */
@Getter
@Setter
@AllArgsConstructor
public class CopyInfo {
    private long operateId;
    private Integer nodeId;
    private String nodeInfo;
    private String key;
}
