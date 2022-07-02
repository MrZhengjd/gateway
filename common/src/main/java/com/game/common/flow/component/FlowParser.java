package com.game.common.flow.component;


import com.game.common.flow.model.Flow;
import com.game.common.flow.model.Request;
import com.game.common.flow.model.Response;

public interface FlowParser {
    Response parseFlow(Flow flow, Request request);
}
