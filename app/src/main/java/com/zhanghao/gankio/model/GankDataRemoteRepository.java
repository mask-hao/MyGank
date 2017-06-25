package com.zhanghao.gankio.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.zhanghao.gankio.R;
import com.zhanghao.gankio.api.GankApi;
import com.zhanghao.gankio.entity.CommonResponse;
import com.zhanghao.gankio.entity.Constant;
import com.zhanghao.gankio.entity.Gank;
import com.zhanghao.gankio.entity.GankContent;
import com.zhanghao.gankio.entity.GankCustom;
import com.zhanghao.gankio.entity.GankFavs;
import com.zhanghao.gankio.entity.GankItem;
import com.zhanghao.gankio.entity.GankSearchItem;
import com.zhanghao.gankio.entity.MoreEntity;
import com.zhanghao.gankio.entity.Tag;
import com.zhanghao.gankio.entity.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import retrofit2.Call;

/**
 * Created by zhanghao on 2017/4/20.
 */

public class GankDataRemoteRepository implements GankDataSource.GankRemoteDataSource{

    private static GankDataRemoteRepository INSTANCE;
    private GankDataRemoteRepository() {}

    public static synchronized GankDataRemoteRepository getInstance() {
        if (INSTANCE == null)
            INSTANCE = new GankDataRemoteRepository();
        return INSTANCE;
    }


    @Override
    public Observable<GankItem> getTypeData(String type, String page, User user) {
        return GankApi.getInstance().service.getTypeData(type,page,user);
    }

    @Override
    public Observable<Gank> getDailyData(String [] array, User user) {
        String year=array[0];
        String month=array[1];
        String day=array[2];
        return GankApi.getInstance().service.getDailyData(year,month,day,user);
    }

    @Override
    public Observable<CommonResponse<String>> deleteOneFav(User user, String _id) {
        return GankApi.getInstance().service.deleteFav(user,_id);
    }


    @Override
    public Observable<CommonResponse<String>> getDateByPage(String page) {
        return GankApi.getInstance().service.getDateByPage(page);
    }

    @Override
    public Observable<CommonResponse<String>> addOneFav(GankContent item, String token) {
        return GankApi.getInstance().service.addOneFav(item,token);
    }

    @Override
    public Observable<GankFavs> getFavs(User user,String type, String page, String count) {
        return GankApi.getInstance().service.getFavs(user,type,page,count);
    }

    @Override
    public Call<CommonResponse<String>> getStartImage() {
        return GankApi.getInstance().service.getStartImage();
    }

    @Override
    public Observable<CommonResponse<List<GankSearchItem>>> getSearchResult(String words) {
        return GankApi.getInstance().service.getSearch(words);
    }

    @Override
    public Observable<Void> addOneHis(GankContent content, String token) {
        return GankApi.getInstance().service.addHis(content,token);
    }

    @Override
    public Observable<GankCustom> getCustomData(User user) {
        return GankApi.getInstance().service.getCustomData(user);
    }

    @Override
    public Observable<CommonResponse<List<Tag>>> getRemoteTags() {
        return GankApi.getInstance().service.getAllTags();
    }


    @Override
    public Observable<GankCustom> getCustomRandomData(List<Tag> tags) {
        return GankApi.getInstance().service.getCustomRandomData(tags);
    }

    @Override
    public Observable<CommonResponse<String>> addUserTags(List<Tag> tags,String token) {
        return GankApi.getInstance().service.addUserTags(tags,token);
    }
}
