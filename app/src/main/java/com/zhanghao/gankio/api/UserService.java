package com.zhanghao.gankio.api;

import com.zhanghao.gankio.entity.CommonResponse;
import com.zhanghao.gankio.entity.User;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by zhanghao on 2017/4/22.
 */

public interface UserService{
    @POST("user/login")
    Observable<CommonResponse<User>> login(@Body User user);

    @POST("user/register")
    Observable<CommonResponse<String>> register(@Body User user);

    @POST("user/logout")
    Observable<CommonResponse<String>> logout(@Body User user);

    @Multipart
    @POST("upload/userImage")
    Observable<CommonResponse<String>> uploadImg(@Part MultipartBody.Part file, @Part MultipartBody.Part token);

    @POST("verification/sendEmail")
    Observable<CommonResponse<String>> getVerifyCode(@Body User user);



}
