package com.dtflys.forest.result;

import com.dtflys.forest.handler.ResultHandler;
import com.dtflys.forest.http.ForestRequest;
import com.dtflys.forest.http.Res;
import com.dtflys.forest.reflection.MethodLifeCycleHandler;
import com.dtflys.forest.utils.ReflectUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

public class FutureResultHandler implements ResultTypeHandler {
    
    @Override
    public boolean matchType(Class<?> resultClass, Type resultType) {
        if (Future.class.isAssignableFrom(resultClass) && resultType instanceof ParameterizedType) {
            final ParameterizedType parameterizedType = (ParameterizedType) resultType;
            final Class<?> rowClass = (Class<?>) parameterizedType.getRawType();
            return Future.class.isAssignableFrom(rowClass);
        }
        return false;
    }

    @Override
    public Object getResult(Optional<?> resultOpt, ForestRequest request, Res response, Type resultType, Class resultClass, ResultHandler resultHandler) {
        final ParameterizedType parameterizedType = (ParameterizedType) resultType;
        final Type realType = parameterizedType.getActualTypeArguments()[0];
        final Class<?> realClass = ReflectUtils.toClass(parameterizedType.getActualTypeArguments()[0]);
        if (realClass == null) {
            return ((MethodLifeCycleHandler<?>) request.getLifeCycleHandler()).getResultData();
        }
        return resultHandler.getResult(resultOpt, request, response, realType, realClass);
    }

    @Override
    public boolean isStream(Class<?> resultClass, Type resultType) {
        return false;
    }

    @Override
    public Object of(Res res, Object rawData, Type targetType) {
        return CompletableFuture.completedFuture(rawData);
    }
}
