package com.game.common.model.msg;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author zheng
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TMessage implements Serializable {

    private int crcCode;
    private int headLength;
    private int bodyLength;
    private THeader tHeader;
    private Object body;
}
