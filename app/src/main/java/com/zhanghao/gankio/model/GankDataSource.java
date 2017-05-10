package com.zhanghao.gankio.model;

import android.content.Context;

import com.zhanghao.gankio.entity.CommonResponse;
import com.zhanghao.gankio.entity.Gank;
import com.zhanghao.gankio.entity.GankContent;
import com.zhanghao.gankio.entity.GankFavs;
import com.zhanghao.gankio.entity.GankItem;
import com.zhanghao.gankio.entity.GankSearchItem;
import com.zhanghao.gankio.entity.MoreEntity;
import com.zhanghao.gankio.entity.User;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import retrofit2.Call;

/**
 * Created by zhanghao on 2017/4/21.
 */

public interface GankDataSource {

    Observable<GankItem> getTypeData(String type, String page, User user);

    Observable<Gank> getDailyData(String [] date, User user);

    Observable<CommonResponse<String>> getDateByPage(String page);

    Observable<CommonResponse<String>> addOneFav(GankContent item,String token);

    Observable<CommonResponse<String>> deleteOneFav(User user,String _id);

    Observable<GankFavs> getFavs(User user,String type,String page,String count);

    Call<CommonResponse<String>> getStartImage();

/*------------------------------------------------------------------------------------------*/

    void initLocalBD(Context context);

    List<MoreEntity> getMoreData(Context context);

    void updateMoreData(Context context,List<MoreEntity> list);

    List<String> getSearchHistory(Context context);

    void updateSearchHistory(Context context,String value);

    void deleteOneHistory(Context context,String value);

    void deleteAllHistory(Context context);

    Observable<CommonResponse<List<GankSearchItem>>> getSearchResult(String words);

}
