package com.zhanghao.gankio.presenter;

import android.content.Context;

import com.zhanghao.gankio.contract.GankContract;
import com.zhanghao.gankio.entity.MoreEntity;
import com.zhanghao.gankio.model.GankDataSource;

import java.util.List;

/**
 * Created by zhanghao on 2017/6/13.
 */

public class GankLocalPresenter extends BasePresenterImpl implements  GankContract.MorePresenter,GankContract.SearchLocalPresenter{



    private GankContract.MoreView mMoreView;
    private GankContract.SearchLocalView mSearchView;
    private GankDataSource.GankLocalDataSource dataSource;


   public GankLocalPresenter(GankContract.MoreView moreView,GankDataSource.GankLocalDataSource dataSource){
        this.mMoreView=moreView;
        this.dataSource=dataSource;
        this.mMoreView.setPresenter(this);
    }

   GankLocalPresenter(GankContract.SearchLocalView localView,GankDataSource.GankLocalDataSource dataSource){
        this.mSearchView=localView;
        this.dataSource=dataSource;
   }



    @Override
    public void getMoreData(Context context) {
        dataSource.initLocalBD(context);
        List<MoreEntity> moreEntityList=dataSource.getMoreData(context);
        mMoreView.setUpMoreData(moreEntityList);
    }

    @Override
    public void updateMoreData(Context context,List<MoreEntity> list) {
        dataSource.updateMoreData(context,list);
    }



    @Override
    public void getSearchHistory(Context context) {
        List<String> list=dataSource.getSearchHistory(context);
        mSearchView.setUpSearchHistory(list);
    }


    @Override
    public void deleteOneHistory(Context context, String value) {
        dataSource.deleteOneHistory(context,value);
    }

    @Override
    public void deleteAllHistory(Context context) {
        dataSource.deleteAllHistory(context);
    }

    @Override
    public void updateSearchHistory(Context context, String word) {
        dataSource.updateSearchHistory(context,word);
    }
}
