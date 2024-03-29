package com.game.common.model.vo;

import com.game.common.model.MasterChangeInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author zheng
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MasterChangeVo {
    private Integer key;
    private MasterChangeInfo first;
    private MasterChangeInfo second;
    private long latestOperateId;
}
