package com.dtflys.forest.result;

import java.util.ArrayList;
import java.util.List;

public class ResultTypeHandlerManager {
    
    private final List<ResultTypeHandler> resultTypeHandlers = new ArrayList<>();
    
    public void registerHandler(ResultTypeHandler resultTypeHandler) {
        resultTypeHandlers.add(resultTypeHandler);
    }

    public List<ResultTypeHandler> getHandlers() {
        return resultTypeHandlers;
    }
}
