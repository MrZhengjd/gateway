package com.game.gateway.model;



import com.game.common.model.DefaultGameMessage;
import lombok.Getter;
import lombok.Setter;

/**
 * @author zheng
 */
@Getter
@Setter
public class ChuPaiMessage extends DefaultGameMessage {
    private BaseChuPaiInfo chuPaiInfo;

}
