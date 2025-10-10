package com.dtflys.forest.result;

import java.lang.reflect.Type;
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
    
    public ResultTypeHandler matchHandler(Class<?> resultClass, Type resultType) {
        for (final ResultTypeHandler handler : resultTypeHandlers) {
            if (handler.matchType(resultClass, resultType)) {
                return handler;
            }
        }
        return null;
    }
}
