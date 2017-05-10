package com.zhanghao.gankio.api;
import com.zhanghao.gankio.entity.CommonResponse;
import com.zhanghao.gankio.entity.Gank;
import com.zhanghao.gankio.entity.GankContent;
import com.zhanghao.gankio.entity.GankFavs;
import com.zhanghao.gankio.entity.GankItem;
import com.zhanghao.gankio.entity.GankSearchItem;
import com.zhanghao.gankio.entity.User;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by zhanghao on 2017/4/20.
 */

public interface GankService {
    @POST("/GankWeb/api/data/{type}/{page}")
    Observable<GankItem> getTypeData
            (
                @Path("type") String type,
                @Path("page") String page,
                @Body User user
            );

    @POST("/GankWeb/api/day/{year}/{month}/{day}")
    Observable<Gank> getDailyData
            (
                @Path("year") String year,
                @Path("month")String month,
                @Path("day")String day,
                @Body User user
            );


    @GET("/GankWeb/api/getDate/{page}")
    Observable<CommonResponse<String>> getDateByPage
            (
                 @Path("page") String page
            );

    @POST("/GankWeb/api/addFav/{token}")
    Observable<CommonResponse<String>> addOneFav(
            @Body GankContent item,
            @Path("token")String token
    );



    @POST("/GankWeb/api/deleteFav/{id}")
    Observable<CommonResponse<String>> deleteFav(
            @Body User user,
            @Path("id")String id
    );


    @POST("GankWeb/api/getFav/{type}/{page}/{count}")
    Observable<GankFavs> getFavs(
            @Body User user,
            @Path("type")String type,
            @Path("page")String page,
            @Path("count")String count
    );


    @GET("GankWeb/api/search/{word}")
    Observable<CommonResponse<List<GankSearchItem>>>
    getSearch(
            @Path("word")String word
    );


    @GET("GankWeb/api/start-img")
    Call<CommonResponse<String>> getStartImage();

}
