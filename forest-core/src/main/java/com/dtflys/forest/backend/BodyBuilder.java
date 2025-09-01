package com.dtflys.forest.backend;

import com.dtflys.forest.handler.LifeCycleHandler;
import com.dtflys.forest.http.ForestRequest;

/**
 * @author gongjun[jun.gong@thebeastshop.com]
 * @since 2017-05-19 14:50
 */
public interface BodyBuilder<R> {

    /**
     * 构建请求体
     * @param req 后端http请求对象
     * @param request Forest请Cont求对象
     * @param lifeCycleHandler 生命周期处理器
     */
    void buildBody(R req, ForestRequest request, LifeCycleHandler lifeCycleHandler);
}
