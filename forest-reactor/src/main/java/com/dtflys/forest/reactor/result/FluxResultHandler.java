package com.dtflys.forest.reactor.result;

import com.dtflys.forest.backend.ContentType;
import com.dtflys.forest.converter.ForestConverter;
import com.dtflys.forest.exceptions.ForestRuntimeException;
import com.dtflys.forest.handler.ResultHandler;
import com.dtflys.forest.http.ForestRequest;
import com.dtflys.forest.http.Res;
import com.dtflys.forest.result.ResultTypeHandler;
import com.dtflys.forest.result.ResultTypeHandlerManager;
import com.dtflys.forest.utils.ForestDataType;
import com.dtflys.forest.utils.ReflectUtils;
import com.dtflys.forest.utils.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.Optional;

public class FluxResultHandler implements ResultTypeHandler {


    @Override
    public boolean matchType(Class<?> resultClass, Type resultType) {
        return Flux.class.isAssignableFrom(resultClass);
    }
    
    @Override
    public Object getResult(Optional<?> resultOpt, ForestRequest request, Res response, Type resultType, Class resultClass, ResultHandler resultHandler) throws Exception {
        final String charsetStr = Optional.ofNullable(response.getCharset()).orElse("UTF-8");
        final Charset charset = Charset.forName(charsetStr);
        final ContentType contentType = response.getContentType();
        final boolean isSSE = contentType != null && ContentType.TEXT_EVENT_STREAM.equals(contentType.toStringWithoutParameters());
        if (resultType instanceof ParameterizedType) {
            final ParameterizedType parameterizedType = (ParameterizedType) resultType;
            final Type subType = parameterizedType.getActualTypeArguments()[0];
            final Class<?> subClass = ReflectUtils.toClass(subType);
            if (CharSequence.class.isAssignableFrom(subClass)) {
                if (isSSE) {
                    return convertToSSELineFlux(response, charset);
                }
                return convertToStringFlux(response, charset, 128);
            } else {
                if (isSSE) {
                    return convertToSSELineFluxWithSubType(request, response, charset, subClass, subType);
                } else {
                    return convertToFluxWithSubType(request, response, subType);
                }
            }
        }
        return convertToStringFlux(response, charset, 128);
    }

    @Override
    public boolean isStream(Class<?> resultClass, Type resultType) {
        return true;
    }

    @Override
    public Object of(Res res, Object rawData, Type targetType) {
        return rawData == null ? Flux.empty() : Flux.just(rawData);
    }


    private static Flux<?> convertToFluxWithSubType(ForestRequest request, Res response, Type subType) {
        final ForestConverter converter = request.getConfiguration().getConverter(ForestDataType.AUTO);
        return Flux.using(
                // 资源工厂：创建 InputStreamReader
                () -> response.getInputStream(),
                // Flux 工厂：按缓冲区读取数据块
                in -> Flux.create(sink -> {
                    try {
                        Object result = converter.convertToJavaObject(in, subType);
                        sink.next(result);
                        sink.complete(); // 流正常结束
                    } catch (Throwable e) {
                        sink.error(e); // 流错误处理
                    }
                }),
                // 资源清理：关闭 Reader（会自动关闭底层 InputStream）
                in -> {
                    try {
                        in.close();
                    } catch (IOException e) {
                        throw new ForestRuntimeException(e);
                    }
                }
        );
    }

    private static Flux<?> convertToSSELineFluxWithSubType(ForestRequest request, Res response, Charset charset, Class<?> subClass, Type subType) {
        final ResultTypeHandlerManager resultTypeHandlerManager = request.getConfiguration().getResultTypeHandlerManager();
        final ForestConverter converter = request.getConfiguration().getConverter(ForestDataType.AUTO);
        return Flux.using(
                // 资源工厂：创建 InputStreamReader
                () -> new BufferedReader(new InputStreamReader(response.getInputStream(), charset)),
                // Flux 工厂：按缓冲区读取数据块
                reader -> Flux.create(sink -> {
                    try {
                        String line;
                        // 循环读取数据块，每次读取最多 bufferSize 个字符
                        while ((line = reader.readLine()) != null) {
                            if (StringUtils.isNotBlank(line)) {
                                final ResultTypeHandler resultTypeHandler = resultTypeHandlerManager.matchHandler(subClass, subType);
                                final Object result = resultTypeHandler != null ?
                                        resultTypeHandler.of(response, line, subType) :
                                        converter.convertToJavaObject(line, subType);
                                sink.next(result); // 发射数据块
                            }
                        }
                        sink.complete(); // 流正常结束
                    } catch (Throwable e) {
                        sink.error(e); // 流错误处理
                    }
                }),
                // 资源清理：关闭 Reader（会自动关闭底层 InputStream）
                reader -> {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        throw new ForestRuntimeException(e);
                    }
                }
        );
    }

    private static Flux<String> convertToSSELineFlux(Res response, Charset charset) {
        return Flux.using(
                // 资源工厂：创建 InputStreamReader
                () -> new BufferedReader(new InputStreamReader(response.getInputStream(), charset)),
                // Flux 工厂：按缓冲区读取数据块
                reader -> Flux.create(sink -> {
                    try {
                        String line;
                        // 循环读取数据块，每次读取最多 bufferSize 个字符
                        while ((line = reader.readLine()) != null) {
                            sink.next(line); // 发射数据块
                        }
                        sink.complete(); // 流正常结束
                    } catch (IOException e) {
                        sink.error(e); // 流错误处理
                    }
                }),
                // 资源清理：关闭 Reader（会自动关闭底层 InputStream）
                reader -> {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        throw new ForestRuntimeException(e);
                    }
                }
        );
    }

    private static Flux<String> convertToStringFlux(Res response, Charset charset, int bufferSize) {
        return Flux.using(
                // 资源工厂：创建 InputStreamReader
                () -> new InputStreamReader(response.getInputStream(), charset),
                // Flux 工厂：按缓冲区读取数据块
                reader -> Flux.create(sink -> {
                    final char[] buffer = new char[bufferSize];
                    try {
                        int numRead;
                        // 循环读取数据块，每次读取最多 bufferSize 个字符
                        while ((numRead = reader.read(buffer, 0, buffer.length)) != -1) {
                            final String chunk = new String(buffer, 0, numRead);
                            sink.next(chunk); // 发射数据块
                        }
                        sink.complete(); // 流正常结束
                    } catch (IOException e) {
                        sink.error(e); // 流错误处理
                    }
                }),
                // 资源清理：关闭 Reader（会自动关闭底层 InputStream）
                reader -> {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        throw new ForestRuntimeException(e);
                    }
                }
        );
    }

}
