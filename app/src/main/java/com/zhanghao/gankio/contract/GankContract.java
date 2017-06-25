package com.zhanghao.gankio.contract;

import android.content.Context;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.zhanghao.gankio.entity.CommonResponse;
import com.zhanghao.gankio.entity.GankCustom;
import com.zhanghao.gankio.entity.GankFavContent;
import com.zhanghao.gankio.entity.GankSearchItem;
import com.zhanghao.gankio.entity.MoreEntity;
import com.zhanghao.gankio.entity.Tag;
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




    //搜索页内容
    interface SearchRemoteView extends BaseView{
        void setUpSearchResult(String searchWord,List<GankSearchItem> datas,boolean isLoadMore);
    }


    interface SearchLocalView extends BaseView{
        void setUpSearchHistory(List<String> histories);
    }

    interface SearchLocalPresenter extends BasePresenter{
        void getSearchHistory(Context context);
        void updateSearchHistory(Context context,String word);
        void deleteOneHistory(Context context,String value);
        void deleteAllHistory(Context context);
    }

    interface SearchRemotePresenter extends BasePresenter{
        void getSearchResult(Context context,String word,boolean isLoadMore);
    }




    //推荐页内容
    interface RecommendView extends BaseView<RecommendPresenter>{
        void setRecommendData(List<MultiItemEntity> customList,boolean refresh);
        void setRecommendTagsData(List<Tag> tags);
    }


    interface RecommendDataSyncView extends BaseView<RecommendPresenter>{
        void dataSyncSuccess(boolean sync);
    }



    interface RecommendRemotePresenter extends BaseGankPresenter{
        void getCustomData(User user,boolean firstLoad);
        void getCustomRandomData(List<Tag> tags ,boolean firstLoad);
        void getRemoteTags();
        void updateUserTags(List<Tag> tags,String token);
    }


    interface RecommendLocalPresenter extends BasePresenter{
        List<Tag> getLocalTags(Context context);
        void saveLocalTags(Context context,List<Tag> tags);
    }


    interface RecommendPresenter extends BaseGankPresenter{
        void getCommonRecommend(User user,Context context,boolean refresh);
        List<Tag> getLocalTags(Context context);
        void saveLocalTags(Context context,List<Tag> tags);
    }






}
