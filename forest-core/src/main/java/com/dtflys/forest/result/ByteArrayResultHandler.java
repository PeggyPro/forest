package com.dtflys.forest.result;

import com.dtflys.forest.handler.ResultHandler;
import com.dtflys.forest.http.ForestRequest;
import com.dtflys.forest.http.Res;

import java.lang.reflect.Type;
import java.util.Optional;

public class ByteArrayResultHandler implements ResultTypeHandler {
    @Override
    public boolean matchType(Class<?> resultClass, Type resultType) {
        return resultClass.isArray() && byte[].class.isAssignableFrom(resultClass);
    }

    @Override
    public Object getResult(Optional<?> resultOpt, ForestRequest request, Res response, Type resultType, Class resultClass, ResultHandler resultHandler) throws Exception {
        return response.getByteArray();
    }

    @Override
    public boolean isReceiveStream(Class<?> resultClass, Type resultType) {
        return false;
    }
}
