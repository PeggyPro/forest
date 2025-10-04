package com.dtflys.forest.result;

import com.dtflys.forest.handler.ResultHandler;
import com.dtflys.forest.http.ForestRequest;
import com.dtflys.forest.http.ForestResponse;
import com.dtflys.forest.http.Res;
import com.dtflys.forest.http.UnclosedResponse;
import com.dtflys.forest.utils.ReflectUtils;

import java.io.InputStream;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Optional;

public class ResponseResultHandler implements ResultTypeHandler {
    
    @Override
    public boolean matchType(Class<?> resultClass, Type resultType) {
        return Res.class.isAssignableFrom(resultClass)
                || ForestRequest.class.isAssignableFrom(resultClass)
                || Optional.class.isAssignableFrom(resultClass);
    }

    @Override
    public Object getResult(Optional<?> resultOpt, ForestRequest request, Res response, Type resultType, Class resultClass, ResultHandler resultHandler) {
        if (resultType instanceof ParameterizedType) {
            final ParameterizedType parameterizedType = (ParameterizedType) resultType;
            final Class<?> rowClass = (Class<?>) parameterizedType.getRawType();
            if (Res.class.isAssignableFrom(rowClass)
                    || ForestRequest.class.isAssignableFrom(resultClass)
                    || Optional.class.isAssignableFrom(rowClass)) {

                final Type realType = parameterizedType.getActualTypeArguments()[0];
                Class<?> realClass = ReflectUtils.toClass(parameterizedType.getActualTypeArguments()[0]);
                if (realClass == null) {
                    realClass = String.class;
                }
                if (!(UnclosedResponse.class.isAssignableFrom(rowClass))) {
                    final Object realResult = resultHandler.getResult(resultOpt, request, response, realType, realClass);
                    response.setResult(realResult);
                }
            }
        }
        return response;

    }

    @Override
    public boolean isReceiveStream(Class<?> resultClass, Type resultType) {
        final boolean isResultResponse = ForestResponse.class.isAssignableFrom(resultClass);
        final boolean isResultOptional = Optional.class.isAssignableFrom(resultClass);

        if (isResultResponse || isResultOptional) {
            ParameterizedType parameterizedType = ReflectUtils.toParameterizedType(resultType);
            if (parameterizedType != null) {
                final Type argType = parameterizedType.getActualTypeArguments()[0];
                final Class<?> argClass = ReflectUtils.toClass(argType);
                if (InputStream.class.isAssignableFrom(argClass)) {
                    return true;
                }
                if (Object.class.equals(argClass) && isResultResponse) {
                    return true;
                }
            } else if (isResultResponse) {
                return false;
            }
        }
        return false;
    }
}
