package com.game.common.model.vo;

import com.game.common.model.msg.THeader;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author zheng
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GameMessageVo implements Serializable {
    private THeader header;
    private Object data;


}
