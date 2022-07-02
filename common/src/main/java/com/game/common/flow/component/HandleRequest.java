package com.game.common.flow.component;


import com.game.common.flow.model.Request;
import com.game.common.flow.model.Response;

public interface HandleRequest {
    Response handleRequest(Request request);
}
