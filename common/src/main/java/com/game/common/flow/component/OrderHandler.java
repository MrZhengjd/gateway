package com.game.common.flow.component;


import com.game.common.flow.model.Request;
import com.game.common.flow.model.Response;

import java.util.Map;

public class OrderHandler implements HandleRequest {
    @Override
    public Response handleRequest(Request request) {
        Map<String,Object> requestMap = request.getRequestMap();
        if (requestMap.containsKey("order")){
            requestMap.put("order",34);
        }
        Response response = new Response();
        response.setRequestMap(requestMap);
        return response;
    }
}
