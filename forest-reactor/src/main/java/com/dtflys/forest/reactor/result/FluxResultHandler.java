package com.dtflys.forest.reactor.result;

import com.dtflys.forest.exceptions.ForestRuntimeException;
import com.dtflys.forest.handler.ResultHandler;
import com.dtflys.forest.http.ForestRequest;
import com.dtflys.forest.http.ForestResponse;
import com.dtflys.forest.http.Res;
import com.dtflys.forest.result.ResultTypeHandler;
import com.dtflys.forest.utils.ReflectUtils;
import reactor.core.publisher.Flux;

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
        if (resultType instanceof ParameterizedType) {
            final ParameterizedType parameterizedType = (ParameterizedType) resultType;
            final Type subType = parameterizedType.getActualTypeArguments()[0];
            final Class<?> subClass = ReflectUtils.toClass(subType);
            if (CharSequence.class.isAssignableFrom(subClass)) {
                return convertToFlux(response, charset, 128);
            }
        }
        return convertToFlux(response, charset, 128);
    }

    @Override
    public boolean isReceiveStream(Class<?> resultClass, Type resultType) {
        return true;
    }

    public static Flux<String> convertToFlux(Res response, Charset charset, int bufferSize) {
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
