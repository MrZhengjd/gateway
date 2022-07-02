package com.game.common.flow.component;

import com.game.common.flow.model.Response;
import com.game.common.model.PlayerRequest;

/**
 * @author zheng
 */
public class PlayerContext {
    private PlayerRequest playerRequest;
    private Response response;

    public PlayerRequest getPlayerRequest() {
        return playerRequest;
    }

    public void setPlayerRequest(PlayerRequest playerRequest) {
        this.playerRequest = playerRequest;
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }
}
