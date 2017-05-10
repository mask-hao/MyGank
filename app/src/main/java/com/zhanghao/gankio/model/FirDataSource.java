package com.zhanghao.gankio.model;

import com.zhanghao.gankio.entity.AppInfo;

import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * Created by zhanghao on 2017/5/9.
 */

public interface FirDataSource {
    Call<AppInfo> getAppInfo();
    Call<ResponseBody> downloadApk(String url);
}
