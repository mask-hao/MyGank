package com.zhanghao.gankio.util;


import com.zhanghao.gankio.BaseApplication;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by zhanghao on 2017/3/17.
 */

public class OkHttpUtil {

    private static final String TAG = "OkHttpUtil";
    private static final String CACHE_PATH="gankCache";
    private static final int CACHE_SIZE=1024*1024*30;
    private volatile static OkHttpClient okHttpClient=null;

    private static Cache cache;

    public static OkHttpClient getInstance(){
        if (okHttpClient==null){
            synchronized (OkHttpUtil.class){
                if (okHttpClient==null){
                    initCache();
                    initialize();
                }
            }
        }
        return okHttpClient;
    }

    private static void initCache() {
        File file=new File(BaseApplication.getContext().getCacheDir(),CACHE_PATH);
        cache=new Cache(file,CACHE_SIZE);
    }

    private  static final Interceptor REWRITE_CACHE_CONTROL_INTERCEPTOR= new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Response originResponse=chain.proceed(chain.request());
            if (NetWorkUtil.isNetWorkAvailable(BaseApplication.getContext())){
                //如果有网络
                LogUtil.d(TAG, "intercept: 有网络");
                int maxAge=60;//一分钟
                return  originResponse.newBuilder()
                        .removeHeader("Pragma")
                        .removeHeader("Cache-Control")
                        .header("Cache-Control", "public, max-age=" + maxAge)
                        .build();
            }else{
                LogUtil.d(TAG, "intercept: 无网络");
                int maxAge=60*60*24*7;//一周
                return originResponse.newBuilder()
                        .removeHeader("Pragma")
                        .removeHeader("Cache-Control")
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + maxAge)
                        .build();
            }
        }
    };

    private static OkHttpClient initialize(){
       return okHttpClient=new OkHttpClient.Builder()
               .addInterceptor(new Interceptor() {
                   @Override
                   public Response intercept(Chain chain) throws IOException {
                       Request request=chain.request();
                       if (!NetWorkUtil.isNetWorkAvailable(BaseApplication.getContext())){
                           request.newBuilder().cacheControl(CacheControl.FORCE_CACHE).build();
                       }
                       return chain.proceed(request);
                   }
               })
                .addNetworkInterceptor(REWRITE_CACHE_CONTROL_INTERCEPTOR)
                .cache(cache)
                .connectTimeout(6, TimeUnit.SECONDS)
                .build();
    }

}
