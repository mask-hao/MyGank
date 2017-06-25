package com.zhanghao.gankio.api;
import com.zhanghao.gankio.entity.CommonResponse;
import com.zhanghao.gankio.entity.Gank;
import com.zhanghao.gankio.entity.GankContent;
import com.zhanghao.gankio.entity.GankCustom;
import com.zhanghao.gankio.entity.GankFavs;
import com.zhanghao.gankio.entity.GankItem;
import com.zhanghao.gankio.entity.GankSearchItem;
import com.zhanghao.gankio.entity.Tag;
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

    @POST("data/{type}/{page}")
    Observable<GankItem> getTypeData
            (
                @Path("type") String type,
                @Path("page") String page,
                @Body User user
            );

    @POST("day/{year}/{month}/{day}")
    Observable<Gank> getDailyData
            (
                @Path("year") String year,
                @Path("month")String month,
                @Path("day")String day,
                @Body User user
            );


    @GET("getDate/{page}")
    Observable<CommonResponse<String>> getDateByPage
            (
                 @Path("page") String page
            );

    @POST("addFav/{token}")
    Observable<CommonResponse<String>> addOneFav(
            @Body GankContent item,
            @Path("token")String token
    );



    @POST("deleteFav/{id}")
    Observable<CommonResponse<String>> deleteFav(
            @Body User user,
            @Path("id")String id
    );


    @POST("getFav/{type}/{page}/{count}")
    Observable<GankFavs> getFavs(
            @Body User user,
            @Path("type")String type,
            @Path("page")String page,
            @Path("count")String count
    );


    @GET("search/{word}")
    Observable<CommonResponse<List<GankSearchItem>>>
    getSearch(
            @Path("word")String word
    );


    @GET("start-img")
    Call<CommonResponse<String>> getStartImage();



    @POST("addHis/{token}")
    Observable<Void> addHis(
            @Body GankContent item,
            @Path("token")String token
    );


    @POST("getCustomData")
    Observable<GankCustom> getCustomData(
            @Body User user);

    @POST("getCustomRandomData")
    Observable<GankCustom> getCustomRandomData(
            @Body List<Tag> tags);


    @POST("addTags/{token}")
    Observable<CommonResponse<String>> addUserTags(
            @Body List<Tag> tags,
            @Path("token")String token);

    @GET("getAllTags")
    Observable<CommonResponse<List<Tag>>> getAllTags();





}
