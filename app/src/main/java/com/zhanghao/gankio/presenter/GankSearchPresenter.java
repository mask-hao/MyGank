package com.zhanghao.gankio.presenter;

import android.content.Context;

import com.zhanghao.gankio.contract.GankContract;
import com.zhanghao.gankio.entity.Gank;
import com.zhanghao.gankio.entity.GankSearchItem;
import com.zhanghao.gankio.model.GankDataSource;

import java.util.List;

/**
 * Created by zhanghao on 2017/6/13.
 */

public class GankSearchPresenter extends BasePresenterImpl implements GankContract.SearchRemotePresenter,GankContract.SearchLocalPresenter{

    private GankLocalPresenter localPresenter;
    private GankRemotePresenter remotePresenter;

    public GankSearchPresenter(GankContract.SearchRemoteView searchRemoteView,
                               GankContract.SearchLocalView searchLocalView,
                               GankDataSource.GankLocalDataSource localDataSource,
                               GankDataSource.GankRemoteDataSource remoteDataSource) {
        localPresenter = new GankLocalPresenter(searchLocalView,localDataSource);
        remotePresenter = new GankRemotePresenter(searchRemoteView,remoteDataSource);

    }

    @Override
    public void getSearchHistory(Context context) {
        localPresenter.getSearchHistory(context);
    }

    @Override
    public void updateSearchHistory(Context context, String word) {
        localPresenter.updateSearchHistory(context,word);
    }

    @Override
    public void deleteOneHistory(Context context, String value) {
        localPresenter.deleteOneHistory(context,value);
    }

    @Override
    public void deleteAllHistory(Context context) {
        localPresenter.deleteAllHistory(context);
    }

    @Override
    public void getSearchResult(Context context, String word, boolean isLoadMore) {
        remotePresenter.getSearchResult(context,word,isLoadMore);
    }

}
