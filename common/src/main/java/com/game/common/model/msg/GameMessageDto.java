package com.game.common.model.msg;

import com.game.common.model.anno.GameMessage;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author zheng
 */
@Getter
@Setter
@NoArgsConstructor
public class GameMessageDto implements Serializable {
    private THeader header;
    private byte[] data;

    public static GameMessageDto buildFromGameMessge(GameMessage gameMessage){
        GameMessageDto result = new GameMessageDto();
        result.header = gameMessage.getHeader();
        result.data = gameMessage.getData();
        return result;
    }
}
