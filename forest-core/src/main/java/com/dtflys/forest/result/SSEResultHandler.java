package com.dtflys.forest.result;

import com.dtflys.forest.handler.ResultHandler;
import com.dtflys.forest.http.ForestRequest;
import com.dtflys.forest.http.ForestSSE;
import com.dtflys.forest.http.Res;
import com.dtflys.forest.sse.ForestSSEListener;

import java.lang.reflect.Type;
import java.util.Optional;

public class SSEResultHandler implements ResultTypeHandler {
    
    @Override
    public boolean matchType(Class<?> resultClass, Type resultType) {
        return ForestSSEListener.class.isAssignableFrom(resultClass);
    }

    @Override
    public Object getResult(Optional<?> resultOpt, ForestRequest request, Res response, Type resultType, Class resultClass, ResultHandler resultHandler) {
        if (ForestSSE.class.equals(resultClass)) {
            return request.sse();
        }
        return request.sse(resultClass);
    }

    @Override
    public boolean isReceiveStream(Class<?> resultClass, Type resultType) {
        return true;
    }
}
