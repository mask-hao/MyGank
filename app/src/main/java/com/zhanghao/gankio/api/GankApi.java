package com.zhanghao.gankio.api;

import com.zhanghao.gankio.BaseApplication;
import com.zhanghao.gankio.BuildConfig;
import com.zhanghao.gankio.entity.Constant;
import com.zhanghao.gankio.util.http.CacheInterceptor;
import com.zhanghao.gankio.util.http.LogInterceptor;

import java.io.File;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by zhanghao on 2017/4/20.
 */

public class GankApi {

    private static final String CACHE_PATH="gankCache";
    private static final int CACHE_SIZE=1024*1024*30;


    public GankService service;

    private GankApi() {
        Retrofit retrofit;
        File file = new File(BaseApplication.getContext().getCacheDir(), CACHE_PATH);
        Cache cache = new Cache(file, CACHE_SIZE);
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .addInterceptor(new CacheInterceptor())
                .addNetworkInterceptor(new CacheInterceptor())
                .cache(cache);
        
        if (BuildConfig.DEBUG){
            builder.addInterceptor(new LogInterceptor());
        }

        retrofit = new Retrofit.Builder()
                .baseUrl(Constant.BASE_URL)
                .client(builder.build())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(GankService.class);
    }


    public static GankApi getInstance() {
        return GankHolder.INSTANCE;
    }


    private static class GankHolder {
        private static final GankApi INSTANCE = new GankApi();
    }


}
