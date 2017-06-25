package com.zhanghao.gankio.presenter;

import android.content.Context;

import com.zhanghao.gankio.contract.GankContract;
import com.zhanghao.gankio.entity.Tag;
import com.zhanghao.gankio.model.GankDataSource;

import java.util.List;

/**
 * Created by zhanghao on 2017/6/24.
 */

public class GankDataSyncPresenter extends BasePresenterImpl{

    private GankLocalPresenter localPresenter;
    private GankRemotePresenter remotePresenter;


    public GankDataSyncPresenter(GankDataSource.GankLocalDataSource localDataSource,
                                 GankDataSource.GankRemoteDataSource remoteDataSource,
                                 GankContract.RecommendDataSyncView recommendDataSyncView){
            localPresenter = new GankLocalPresenter(localDataSource);
            remotePresenter = new GankRemotePresenter(recommendDataSyncView,remoteDataSource);
    }


    public void updateUserTags(Context context,String token){
        List<Tag> tags =localPresenter.getLocalTags(context);
        if (tags!=null&&tags.size()>0)
            remotePresenter.updateUserTags(tags,token);
    }


}
