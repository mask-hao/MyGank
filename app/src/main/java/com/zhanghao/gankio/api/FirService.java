package com.zhanghao.gankio.api;

import com.zhanghao.gankio.entity.AppInfo;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by zhanghao on 2017/5/9.
 */

public interface FirService {

    @GET("apps/latest/590f316f548b7a04960000c3?api_token=4b2e178099c71ad131aae5f194b8f07c")
    Call<AppInfo> getAppBuildInfo();


    @GET
    @Streaming
    Call<ResponseBody> downloadNewApk(@Url String url);

}
