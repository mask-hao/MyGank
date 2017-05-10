package com.zhanghao.gankio.model;

import com.zhanghao.gankio.api.FirApi;
import com.zhanghao.gankio.entity.AppInfo;

import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * Created by zhanghao on 2017/5/9.
 */

public class FirDataRepository implements FirDataSource{

    private FirDataRepository(){}

    public  static FirDataRepository getInstance(){
        return  FirDataRepositoryHolder.dataRepository;
    }

    private static class FirDataRepositoryHolder{
        private static final FirDataRepository dataRepository=new FirDataRepository();
    }

    @Override
    public Call<AppInfo> getAppInfo() {
        return FirApi.getInstance().service.getAppBuildInfo();
    }

    @Override
    public Call<ResponseBody> downloadApk(String url) {
        return FirApi.getInstance().service.downloadNewApk(url);
    }
}
