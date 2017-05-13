package com.zhanghao.gankio.util.http;

import android.content.Context;

import com.zhanghao.gankio.BaseApplication;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by zhanghao on 2017/5/12.
 */

public class CacheInterceptor implements Interceptor {

    private Context context=BaseApplication.getContext();

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request=chain.request();
        if (!NetWorkUtil.isNetWorkAvailable(context)){
            request=request.newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build();
        }
        Response response = chain.proceed(request);
        Response latestResponse;
        if (NetWorkUtil.isNetWorkAvailable(context)){

            int maxAge=60;//一分钟
            latestResponse=response.newBuilder()
                    .removeHeader("Pragma")
                    .removeHeader("Cache-Control")
                    .header("Cache-Control", "public, max-age=" + maxAge)
                    .build();
        }else{
            int maxAge=60*60*24*7;//一周
            latestResponse = response.newBuilder()
                    .removeHeader("Pragma")
                    .removeHeader("Cache-Control")
                    .header("Cache-Control", "public, only-if-cached, max-stale=" + maxAge)
                    .build();
        }
        return latestResponse;
    }
}
