package com.zhanghao.gankio.api;

import com.zhanghao.gankio.entity.Constant;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by zhanghao on 2017/4/22.
 */

public class UserApi {

    public UserService userService;
    private UserApi(){
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(Constant.BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        userService=retrofit.create(UserService.class);
    }


    public static UserApi getInstance(){
        return UserHolder.INSTANCE;
    }

    private static class UserHolder{
        private static final UserApi INSTANCE=new UserApi();
    }

}
