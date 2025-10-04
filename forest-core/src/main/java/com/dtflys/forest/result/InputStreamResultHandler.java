package com.dtflys.forest.result;

import com.dtflys.forest.handler.ResultHandler;
import com.dtflys.forest.http.ForestRequest;
import com.dtflys.forest.http.Res;
import com.dtflys.forest.utils.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class InputStreamResultHandler implements ResultTypeHandler {
    
    @Override
    public boolean matchType(Class<?> resultClass, Type resultType) {
        return InputStream.class.isAssignableFrom(resultClass);
    }

    @Override
    public Object getResult(Optional<?> resultOpt, ForestRequest request, Res response, Type resultType, Class resultClass, ResultHandler resultHandler) throws Exception {
        return response.getInputStream();
    }

    @Override
    public boolean isStream(Class<?> resultClass, Type resultType) {
        return true;
    }

    @Override
    public Object of(Res res, Object rawData, Type targetType) {
        if (rawData == null) {
            return null;
        }
        if (rawData instanceof InputStream) {
            return rawData;
        }
        if (rawData instanceof byte[]) {
            return new ByteArrayInputStream((byte[]) rawData);
        }
        final String charsetStr = res.getCharset();
        final Charset charset = StringUtils.isBlank(charsetStr) ? StandardCharsets.UTF_8 : Charset.forName(charsetStr);
        return new ByteArrayInputStream(rawData.toString().getBytes(charset));
    }
}
