package com.dtflys.forest.handler;

import com.dtflys.forest.backend.ContentType;
import com.dtflys.forest.converter.ForestConverter;
import com.dtflys.forest.exceptions.ForestHandlerException;
import com.dtflys.forest.http.ForestRequest;
import com.dtflys.forest.http.ForestResponse;
import com.dtflys.forest.http.ForestSSE;
import com.dtflys.forest.lifecycles.file.DownloadLifeCycle;
import com.dtflys.forest.reflection.MethodLifeCycleHandler;
import com.dtflys.forest.sse.ForestSSEListener;
import com.dtflys.forest.utils.ForestDataType;
import com.dtflys.forest.utils.ReflectUtils;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.Optional;
import java.util.concurrent.Future;

/**
 * @author gongjun[jun.gong@thebeastshop.com]
 * @since 2016-05-04
 */
public class ResultHandler {

    protected boolean isReceivedResponseData(ForestResponse response) {
        if (response == null) {
            return false;
        }
        return response.isReceivedResponseData();
    }

    /**
     * 进行转换并获取结果
     *
     * @param request Forest 请求对象
     * @param response Forest 响应对象
     * @param resultType {@link Type} 实例
     * @return 转换后的对象
     * @since 1.5.27
     */
    public Object getResult(ForestRequest request, ForestResponse response, Type resultType) {
        final Class<?> clazz = ReflectUtils.toClass(resultType);
        return getResult(request, response, resultType, clazz);
    }

    /**
     * 进行转换并获取结果
     *
     * @param request Forest 请求对象
     * @param response Forest 响应对象
     * @param resultClass {@link Class} 实例
     * @return 转换后的对象
     * @since 1.5.27
     */
    public Object getResult(ForestRequest request, ForestResponse response, Class resultClass) {
        final Type type = ReflectUtils.toType(resultClass);
        return getResult(request, response, type, resultClass);
    }


    public Object getResult(ForestRequest request, ForestResponse response, Type resultType, Class resultClass) {
        if (request.isDownloadFile()) {
            return null;
        }
        if (isReceivedResponseData(response)) {
            try {
                if (void.class.isAssignableFrom(resultClass)) {
                    return null;
                }
                if (ForestResponse.class.isAssignableFrom(resultClass)
                        || ForestRequest.class.isAssignableFrom(resultClass)) {
                    if (resultType instanceof ParameterizedType) {
                        final ParameterizedType parameterizedType = (ParameterizedType) resultType;
                        final Class<?> rowClass = (Class<?>) parameterizedType.getRawType();
                        if (ForestResponse.class.isAssignableFrom(rowClass)
                                || ForestRequest.class.isAssignableFrom(resultClass)) {
                            final Type realType = parameterizedType.getActualTypeArguments()[0];
                            Class<?> realClass = ReflectUtils.toClass(parameterizedType.getActualTypeArguments()[0]);
                            if (realClass == null) {
                                realClass = String.class;
                            }
                            final Object realResult = getResult(request, response, realType, realClass);
                            response.setResult(realResult);
                        }
                    }
                    return response;
                }
                if (Future.class.isAssignableFrom(resultClass)) {
                    if (resultType instanceof ParameterizedType) {
                        final ParameterizedType parameterizedType = (ParameterizedType) resultType;
                        final Class<?> rowClass = (Class<?>) parameterizedType.getRawType();
                        if (Future.class.isAssignableFrom(rowClass)) {
                            final Type realType = parameterizedType.getActualTypeArguments()[0];
                            final Class<?> realClass = ReflectUtils.toClass(parameterizedType.getActualTypeArguments()[0]);
                            if (realClass == null) {
                                return ((MethodLifeCycleHandler<?>) request.getLifeCycleHandler()).getResultData();
                            }
                            return getResult(request, response, realType, realClass);
                        }
                    }
                }
                if (ForestSSEListener.class.isAssignableFrom(resultClass)) {
                    if (ForestSSE.class.equals(resultClass)) {
                        return request.sse();
                    }
                    return request.sse(resultClass);
                }
                if (resultClass.isArray()) {
                    if (byte[].class.isAssignableFrom(resultClass)) {
                        return response.getByteArray();
                    }
                }
                final Object attFile = request.getAttachment(DownloadLifeCycle.ATTACHMENT_NAME_FILE);
                if (attFile != null && attFile instanceof File) {
                    final ForestConverter converter = request.getConfiguration().getConverter(ForestDataType.JSON);
                    return converter.convertToJavaObject(attFile, resultClass);
                }
                String responseText = null;
                if (CharSequence.class.isAssignableFrom(resultClass)) {
                    try {
                        responseText = response.readAsString();
                    } catch (Throwable th) {
                        request.getLifeCycleHandler().handleError(request, response, th);
                    }
                }
                else {
                    try {
                        responseText = response.getContent();
                    } catch (Throwable th) {
                        request.getLifeCycleHandler().handleError(request, response, th);
                    }
                }
                response.setContent(responseText);
                if (InputStream.class.isAssignableFrom(resultClass)) {
                    return response.getInputStream();
                }
                final ContentType contentType = response.getContentType();
                final ForestConverter decoder = request.getDecoder();
                if (decoder != null) {
                    if (contentType != null && contentType.canReadAsString()) {
                        return decoder.convertToJavaObject(responseText, resultType);
                    } else {
                        final String charset = response.getCharset();
                        return decoder.convertToJavaObject(
                                response.getByteArray(), resultType, Charset.forName(Optional.ofNullable(charset).orElse("UTF-8")));
                    }
                } else if (CharSequence.class.isAssignableFrom(resultClass)) {
                    return responseText;
                }

                final ForestDataType dataType = request.getDataType();
                final ForestConverter converter = request.getConfiguration().getConverter(dataType);
                if (contentType != null && contentType.canReadAsString()) {
                    return converter.convertToJavaObject(responseText, resultType);
                }
                Charset charset = null;
                String resCharset  = response.getCharset();
                if (resCharset != null) {
                    charset = Charset.forName(resCharset);
                }
                return converter.convertToJavaObject(response.getInputStream(), resultType, charset);
            } catch (Exception e) {
                throw new ForestHandlerException(e, request, response);
            }
        }
        else if (ForestResponse.class.isAssignableFrom(resultClass)) {
            return response;
        }
        return null;
    }

}
