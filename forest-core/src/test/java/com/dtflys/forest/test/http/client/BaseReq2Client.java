package com.dtflys.forest.test.http.client;

import com.dtflys.forest.annotation.BaseRequest;
import com.dtflys.forest.annotation.Get;
import com.dtflys.forest.annotation.Request;
import com.dtflys.forest.annotation.Var;
import com.dtflys.forest.callback.OnSuccess;
import com.dtflys.forest.http.ForestRequest;
import com.dtflys.forest.http.ForestResponse;
import com.dtflys.forest.interceptor.Interceptor;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * @author gongjun[jun.gong@thebeastshop.com]
 * @since 2018-04-09 16:27
 */
@BaseRequest(baseURL = "http://www.xxxx.com")
public interface BaseReq2Client {

    @Get("/test")
    ForestRequest<?> test();

    interface NoneBaseReqClient {

        @Get("/test")
        ForestRequest<?> test();

    }


}
