package com.dtflys.forest.result;

import com.dtflys.forest.handler.ResultHandler;
import com.dtflys.forest.http.ForestRequest;
import com.dtflys.forest.http.Res;

import java.lang.reflect.Type;
import java.util.Optional;

public interface ResultTypeHandler {
    
    boolean matchType(Class<?> resultClass, Type resultType);
    
    Object getResult(Optional<?> resultOpt, ForestRequest request, Res response, Type resultType, Class resultClass, ResultHandler resultHandler) throws Exception;
    
    boolean isReceiveStream(Class<?> resultClass, Type resultType);
}
