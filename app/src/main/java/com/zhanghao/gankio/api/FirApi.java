package com.zhanghao.gankio.api;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by zhanghao on 2017/5/9.
 */

public class FirApi {
    public FirService service;
    private static final String BASE_URL="http://api.fir.im/";


    private FirApi(){
        Retrofit retrofit;
        retrofit=new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(new OkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service=retrofit.create(FirService.class);
    }


    public static FirApi getInstance(){
        return FirApiHolder.firApi;
    }

    private static class FirApiHolder{
        private static  final FirApi firApi=new FirApi();
    }


}
