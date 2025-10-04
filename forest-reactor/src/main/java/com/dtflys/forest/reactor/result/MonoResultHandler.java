package com.dtflys.forest.reactor.result;

import cn.hutool.core.lang.Opt;
import com.dtflys.forest.backend.ContentType;
import com.dtflys.forest.config.ForestConfiguration;
import com.dtflys.forest.converter.ForestConverter;
import com.dtflys.forest.exceptions.ForestRuntimeException;
import com.dtflys.forest.handler.ResultHandler;
import com.dtflys.forest.http.ForestRequest;
import com.dtflys.forest.http.Res;
import com.dtflys.forest.result.ResultTypeHandler;
import com.dtflys.forest.result.ResultTypeHandlerManager;
import com.dtflys.forest.utils.ForestDataType;
import com.dtflys.forest.utils.ReflectUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.Optional;

public class MonoResultHandler implements ResultTypeHandler {
    
    @Override
    public boolean matchType(Class<?> resultClass, Type resultType) {
        return Mono.class.isAssignableFrom(resultClass);
    }

    @Override
    public Object getResult(Optional<?> resultOpt, ForestRequest request, Res response, Type resultType, Class resultClass, ResultHandler resultHandler) throws Exception {
        if (resultType instanceof ParameterizedType) {
            final ParameterizedType parameterizedType = (ParameterizedType) resultType;
            final Type subType = parameterizedType.getActualTypeArguments()[0];
            final Class<?> subClass = ReflectUtils.toClass(subType);
            final Object result = resultHandler.getResult(resultOpt, request, response, subType, subClass);
            if (result == null) {
                return Mono.empty();
            }
            if (result instanceof Optional) {
                if (Optional.class.isAssignableFrom(subClass)) {
                    return Mono.just(result);
                } else {
                    final Optional<?> optional = (Optional<?>) result;
                    return optional.isPresent() ? Mono.just(optional.get()) : Mono.empty();
                }
            }
            return Mono.just(result);
        }
        final String charsetStr = Optional.ofNullable(response.getCharset()).orElse("UTF-8");
        final Charset charset = Charset.forName(charsetStr);
        return convertToStringMono(response, charset, 8);
    }


    private static Mono<String> convertToStringMono(Res response, Charset charset, int bufferSize) throws Exception {
        InputStream inputStream = response.getInputStream();
        return Mono.using(
                // 资源工厂：创建 ByteArrayOutputStream 用于累积数据
                () -> new ByteArrayOutputStream(),
                // Mono 工厂：读取所有数据并转换为字符串
                baos -> Mono.fromCallable(() -> {
                    final byte[] buffer = new byte[bufferSize];
                    int bytesRead;
                    try {
                        // 循环读取所有数据块
                        while ((bytesRead = inputStream.read(buffer)) != -1) {
                            baos.write(buffer, 0, bytesRead); // 累积数据
                        }
                        // 将累积的字节数据转换为字符串
                        return baos.toString(charset.name());
                    } catch (IOException e) {
                        throw new ForestRuntimeException(e);
                    }
                }),
                // 资源清理：关闭 ByteArrayOutputStream
                baos -> {
                    try {
                        inputStream.close();
                        baos.close();
                    } catch (IOException e) {
                        throw new ForestRuntimeException(e);
                    }
                    
                }
        );
    }


    @Override
    public boolean isStream(Class<?> resultClass, Type resultType) {
        return true;
    }

    @Override
    public Object of(Res res, Object rawData, Type targetType) {
        final ForestConfiguration configuration = res.getRequest().getConfiguration();
        final ForestConverter converter = configuration.getConverter(ForestDataType.AUTO);
        final ResultTypeHandlerManager resultTypeHandlerManager = configuration.getResultTypeHandlerManager();
        if (targetType instanceof ParameterizedType) {
            final ParameterizedType parameterizedType = (ParameterizedType) targetType;
            final Type subType = parameterizedType.getActualTypeArguments()[0];
            final Class<?> subClass = ReflectUtils.toClass(subType);
            
            final ResultTypeHandler resultTypeHandler = resultTypeHandlerManager.matchHandler(subClass, subType);
            if (resultTypeHandler != null) {
                return resultTypeHandler.of(res, rawData, subType);
            }
            
            final Object result = converter.convertToJavaObject(rawData, subType);
            return rawData == null ? Mono.empty() : Mono.just(result);
        }
        return rawData == null ? Mono.empty() : Mono.just(rawData);
    }

}
