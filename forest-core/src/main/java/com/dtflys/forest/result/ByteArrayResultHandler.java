package com.dtflys.forest.result;

import com.dtflys.forest.handler.ResultHandler;
import com.dtflys.forest.http.ForestRequest;
import com.dtflys.forest.http.Res;
import com.dtflys.forest.utils.StringUtils;

import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
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
    public boolean isStream(Class<?> resultClass, Type resultType) {
        return false;
    }

    @Override
    public Object of(Res res, Object rawData, Type targetType) {
        if (rawData == null) {
            return null;
        }
        if (rawData instanceof byte[]) {
            return rawData;
        }
        final String charsetStr = res.getCharset();
        final Charset charset = StringUtils.isBlank(charsetStr) ? StandardCharsets.UTF_8 : Charset.forName(charsetStr);
        return rawData.toString().getBytes(charset);
    }
}
