package com.zhanghao.gankio.util.http;

import com.zhanghao.gankio.util.LogUtil;

import java.io.IOException;


import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by zhanghao on 2017/5/12.
 */

public class LogInterceptor implements Interceptor{
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request=chain.request();
        Response response=chain.proceed(request);
        if (response!=null && request.body()!=null){
            MediaType mediaType = response.body().contentType();
            String content = response.body().string();
            LogUtil.d("log-data-response", "" + content);
            return response.newBuilder()
                    .body(ResponseBody.create(mediaType, content))
                    .build();
        }
        return response;
    }
}
