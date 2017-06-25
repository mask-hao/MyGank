package com.zhanghao.gankio.model;

import android.content.Context;

import com.zhanghao.gankio.entity.CommonResponse;
import com.zhanghao.gankio.entity.Gank;
import com.zhanghao.gankio.entity.GankContent;
import com.zhanghao.gankio.entity.GankCustom;
import com.zhanghao.gankio.entity.GankFavs;
import com.zhanghao.gankio.entity.GankItem;
import com.zhanghao.gankio.entity.GankSearchItem;
import com.zhanghao.gankio.entity.MoreEntity;
import com.zhanghao.gankio.entity.Tag;
import com.zhanghao.gankio.entity.User;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Call;

/**
 * Created by zhanghao on 2017/4/21.
 */

public interface GankDataSource {

    interface GankRemoteDataSource {

        Observable<GankItem> getTypeData(String type, String page, User user);

        Observable<Gank> getDailyData(String[] date, User user);

        Observable<CommonResponse<String>> getDateByPage(String page);

        Observable<CommonResponse<String>> addOneFav(GankContent item, String token);

        Observable<CommonResponse<String>> deleteOneFav(User user, String _id);

        Observable<GankFavs> getFavs(User user, String type, String page, String count);

        Call<CommonResponse<String>> getStartImage();

        Observable<CommonResponse<List<GankSearchItem>>> getSearchResult(String words);



        Observable<Void> addOneHis(GankContent content,String token);

        Observable<GankCustom> getCustomData(User user);

        Observable<GankCustom> getCustomRandomData(List<Tag> tags);

        Observable<CommonResponse<List<Tag>>> getRemoteTags();

        Observable<CommonResponse<String>> addUserTags(List<Tag> tags,String token);


    }

    interface GankLocalDataSource {
        void initLocalBD(Context context);

        List<MoreEntity> getMoreData(Context context);

        void updateMoreData(Context context, List<MoreEntity> list);

        List<String> getSearchHistory(Context context);

        void updateSearchHistory(Context context, String value);

        void deleteOneHistory(Context context, String value);

        void deleteAllHistory(Context context);

        void saveLocalTags(Context context,List<Tag> tags);

        List<Tag> getLocalTags(Context context);

    }

}
