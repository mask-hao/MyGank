package com.zhanghao.gankio.api;

import com.zhanghao.gankio.entity.Constant;
import com.zhanghao.gankio.util.OkHttpUtil;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by zhanghao on 2017/4/20.
 */

public class GankApi {

    public GankService service;

    private GankApi() {
        Retrofit retrofit;
        OkHttpClient client = OkHttpUtil.getInstance();
        retrofit = new Retrofit.Builder()
                .baseUrl(Constant.BASE_URL)
                .client(client)
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
