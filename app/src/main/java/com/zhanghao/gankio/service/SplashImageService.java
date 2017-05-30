package com.zhanghao.gankio.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.zhanghao.gankio.entity.CommonResponse;
import com.zhanghao.gankio.model.GankDataRepository;
import com.zhanghao.gankio.model.GankDataSource;
import com.zhanghao.gankio.util.SharedPrefsUtils;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by zhanghao on 2017/5/8.
 */

public class SplashImageService extends IntentService{
    public SplashImageService(){
        super("SplashIntentService");
    }

    public SplashImageService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String defaultUrl="https://images.unsplash.com/photo-1494122474412-aeaf73d11da8?w=1080&h=1920";

        GankDataSource source= GankDataRepository.getInstance();
        Call<CommonResponse<String>> call=source.getStartImage();
        try {
           Response<CommonResponse<String>> response = call.execute();
           if (response.isSuccessful()){
               String url=response.body().getContent();
               SharedPrefsUtils.setStringPreference(this,"start-img",url);
               Glide.with(this).load(url).downloadOnly(1920,1080);
           }else{
               SharedPrefsUtils.setStringPreference(this,"start-img",defaultUrl);
            }
        } catch (IOException e) {
            e.printStackTrace();
            SharedPrefsUtils.setStringPreference(this,"start-img",defaultUrl);
        }

    }
}
