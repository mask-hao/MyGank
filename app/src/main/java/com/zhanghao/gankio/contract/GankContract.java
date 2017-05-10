package com.zhanghao.gankio.contract;

import android.content.Context;
import android.view.View;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.zhanghao.gankio.entity.GankFavContent;
import com.zhanghao.gankio.entity.GankItem;
import com.zhanghao.gankio.entity.GankSearchItem;
import com.zhanghao.gankio.entity.GankSection;
import com.zhanghao.gankio.entity.MoreEntity;
import com.zhanghao.gankio.entity.User;
import com.zhanghao.gankio.presenter.BaseGankPresenter;
import com.zhanghao.gankio.presenter.BasePresenter;

import java.util.List;

/**
 * Created by zhanghao on 2017/4/20.
 */

public interface GankContract{


    interface BaseGankView<T extends BaseGankPresenter> extends BaseView<T>{
        void onAddFavSuccess(String message, int pos);
        void onDeleteFavSuccess(String message,int pos);
    }

    //首页内容
     interface DailyView extends BaseGankView<DailyPresenter>{
         void setUpDailyData(List<MultiItemEntity> datas,boolean isRefresh, boolean isLoadMore);
     }

     interface DailyPresenter extends BaseGankPresenter{
         void getDailyData(String page,boolean isRefresh,boolean isLoadMore);
     }

//   ----------------------------

    //type页内容
    interface TypeView extends BaseGankView<TypePresenter>{
        void setUpTypeData(List<MultiItemEntity> datas,boolean isRefresh,boolean isLoadMore);
    }

    interface TypePresenter extends BaseGankPresenter{
        void getTypeData(String type,String page,boolean isRefresh,boolean isLoadMore);
    }


    //收藏内容

    interface FavView extends BaseGankView<FavPresenter>{
        void setUpFavs(List<GankFavContent> data,boolean isLoadMore);
    }


    interface FavPresenter extends BaseGankPresenter{
        void getFavs(User user,String type ,String page,String count,boolean isLoadMore);
    }



    //More内容
    interface MoreView extends BaseView<MorePresenter>{
        void setUpMoreData(List<MoreEntity> moreEntities);
    }

    interface MorePresenter extends BasePresenter{
        void getMoreData(Context context);
        void updateMoreData(Context context,List<MoreEntity> list);
    }


    /*-----------------------------------------------*/


    interface SearchView extends BaseView<SearchPresenter>{
        void setUpSearchResult(List<GankSearchItem> datas,boolean isLoadMore);
        void setUpSearchHistory(List<String> histories);
    }

    interface SearchPresenter extends BasePresenter{
        void getSearchResult(Context context,String word,boolean isLoadMore);
        void getSearchHistory(Context context);
        void deleteOneHistory(Context context,String value);
        void deleteAllHistory(Context context);
    }
}
