package com.zhanghao.gankio.presenter;

import android.content.Context;
import android.util.Log;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.zhanghao.gankio.contract.GankContract;
import com.zhanghao.gankio.entity.CommonResponse;
import com.zhanghao.gankio.entity.Constant;
import com.zhanghao.gankio.entity.Gank;
import com.zhanghao.gankio.entity.GankContent;
import com.zhanghao.gankio.entity.GankCustom;
import com.zhanghao.gankio.entity.GankFavs;
import com.zhanghao.gankio.entity.GankSearchItem;
import com.zhanghao.gankio.entity.GankSection;
import com.zhanghao.gankio.entity.GankItem;
import com.zhanghao.gankio.entity.RecommendPhoto;
import com.zhanghao.gankio.entity.Tag;
import com.zhanghao.gankio.entity.User;
import com.zhanghao.gankio.model.GankDataSource;
import com.zhanghao.gankio.rx.RxHelper;
import com.zhanghao.gankio.rx.RxObserver;
import com.zhanghao.gankio.util.ComUtil;
import com.zhanghao.gankio.util.LogUtil;
import com.zhanghao.gankio.util.RandomUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by zhanghao on 2017/4/20.
 */

public class GankRemotePresenter extends BasePresenterImpl implements
        GankContract.DailyPresenter,
        GankContract.TypePresenter,
        GankContract.FavPresenter,
        GankContract.RecommendRemotePresenter,
        GankContract.SearchRemotePresenter{


    private GankContract.DailyView mDaliyView;
    private GankContract.TypeView mTypeView;
    private GankContract.FavView mFavView;
    private GankContract.SearchRemoteView mSearchView;
    private GankContract.RecommendView mRecommendView;
    private GankContract.RecommendDataSyncView mSyncView;
    private GankDataSource.GankRemoteDataSource dataSource;
    private static final String TAG = "GankPresenter";


    public GankRemotePresenter(GankContract.TypeView TypeView, GankDataSource.GankRemoteDataSource dataSource) {
        this.mTypeView = TypeView;
        this.dataSource = dataSource;
        mTypeView.setPresenter(this);
    }

    public GankRemotePresenter(GankContract.DailyView mView, GankDataSource.GankRemoteDataSource dataSource) {
        this.mDaliyView = mView;
        this.dataSource = dataSource;
        mDaliyView.setPresenter(this);
    }

    public GankRemotePresenter(GankContract.FavView mFavView, GankDataSource.GankRemoteDataSource dataSource) {
        this.mFavView = mFavView;
        this.dataSource = dataSource;
        this.mFavView.setPresenter(this);
    }


    public GankRemotePresenter(GankContract.RecommendView recommendView,
                               GankDataSource.GankRemoteDataSource dataSource,
                               GankRecommendPresenter presenter){
        this.mRecommendView  = recommendView;
        this.dataSource = dataSource;
        this.mRecommendView.setPresenter(presenter);
    }

    GankRemotePresenter(GankContract.SearchRemoteView searchView, GankDataSource.GankRemoteDataSource dataSource){
        this.mSearchView=searchView;
        this.dataSource=dataSource;
    }



    public GankRemotePresenter(GankContract.RecommendDataSyncView syncView,GankDataSource.GankRemoteDataSource dataSource){
        this.mSyncView = syncView;
        this.dataSource = dataSource;
    }



    @Override
    public void getDailyData(String page, boolean isRefresh,boolean isLoadMore) {
        if (!isLoadMore)
            mDaliyView.showDialog();
        dataSource.getDateByPage(page).compose(RxHelper.toUI())
                .subscribe(new RxObserver<CommonResponse<String>>(this) {
                    @Override
                    protected void onSuccess(CommonResponse<String> stringCommonResponse) {
                        switch (stringCommonResponse.getResult()) {
                            case Constant.GET_DATE_SUCCESS:
                                // TODO: 2017/4/27 获取数据
                                LogUtil.d(TAG,stringCommonResponse.getContent());
                                String[] dateArray = stringCommonResponse.getContent().split("/");
                                getRealData(dateArray, isRefresh,isLoadMore);
                                break;
                            case Constant.GET_DATE_FAILED:
                                onFail(Constant.GET_DATA_FAILED);
                                break;
                        }
                    }

                    @Override
                    protected void onFail(String message) {
                        mDaliyView.showError(message);
                        mDaliyView.hideDialog();
                    }
                });
    }

    private void getRealData(String[] dateArray, boolean isRefresh,boolean isLoadMore) {
        dataSource.getDailyData(dateArray, User.getInstance())
                .compose(RxHelper.toUI())
                .subscribe(new RxObserver<Gank>(this) {
                    @Override
                    protected void onSuccess(Gank gank) {
                        if (!gank.isError()) {
                            setDatatoView(gank,isRefresh,isLoadMore);
                        } else
                            onFail(Constant.GET_DATA_FAILED);
                        mDaliyView.hideDialog();
                    }

                    @Override
                    protected void onFail(String message) {
                        mDaliyView.showError(message);
                    }
                });
    }

    private void setDatatoView(Gank gank,boolean isRefresh,boolean isLoadMore) {
        List<String> c=gank.getCategory();
        c.remove(Constant.PHOTO);
        List<String> categories= ComUtil.getRightCategories(c);

        List<MultiItemEntity> gankDatas=new ArrayList<>();
//        List<GankSection> homePics=new ArrayList<>();

        Gank.ResultsBean resultsBean=gank.getResult();
        GankSection imgSection=new GankSection(Constant.IMG,resultsBean.getPhoto().get(0));
        gankDatas.add(imgSection);
//        homePics.add(imgSection);

        int length=categories.size();
        for (int i = 0; i < length; i++) {
            String str=categories.get(i);
            gankDatas.add(new GankSection(Constant.SECTION,str));
            switch (str){
                case Constant.ANDROID:
                    gankDatas.addAll(resultsBean.getAndroid());
                    break;
                case Constant.IOS:
                    gankDatas.addAll(resultsBean.getiOS());
                    break;
                case Constant.WEB:
                    gankDatas.addAll(resultsBean.getWeb());
                    break;
                case Constant.VIDEO:
                    gankDatas.addAll(resultsBean.getVideo());
                    break;
                case Constant.EXPANDRES:
                    gankDatas.addAll(resultsBean.getExtend());
                    break;
                case Constant.RECOMMEND:
                    gankDatas.addAll(resultsBean.getRecommend());
                    break;
                case Constant.APP:
                    gankDatas.addAll(resultsBean.getApp());
                    break;
            }
        }
        mDaliyView.setUpDailyData(gankDatas,isRefresh, isLoadMore);
    }

    @Override
    public void getTypeData(String type, String page, boolean isRefresh,boolean isLoadMore) {
        if (!isLoadMore)
            mTypeView.showDialog();
        dataSource.getTypeData(type, page, User.getInstance())
                .compose(RxHelper.toUI())
                .subscribe(new RxObserver<GankItem>(this) {
                    @Override
                    protected void onSuccess(GankItem gankItem) {
                        if (!gankItem.isError()) {
                            setTypeDataToView(gankItem,isRefresh,isLoadMore);
//                            mTypeView.setUpTypeData(gankItem, isRefresh,isLoadMore);
                        } else
                            onFail(Constant.GET_DATA_FAILED);
                        mTypeView.hideDialog();
                    }

                    @Override
                    protected void onFail(String message) {
                        mTypeView.showError(message);
                    }
                });
    }

    private void setTypeDataToView(GankItem gankItem, boolean isRefresh, boolean isLoadMore) {
        List<MultiItemEntity> gankDatas=new ArrayList<>();
        gankDatas.addAll(gankItem.getResult());
        mTypeView.setUpTypeData(gankDatas,isRefresh,isLoadMore);
    }





    @Override
    public void addOneFav(GankContent item,String token,int pos) {
        dataSource.addOneFav(item,token)
                .compose(RxHelper.toUI())
                .subscribe(new RxObserver<CommonResponse<String>>(this) {
                    @Override
                    protected void onSuccess(CommonResponse<String> stringCommonResponse) {
                        if (stringCommonResponse.getResult().equals(Constant.ADDFAV_SUCCESS)){
                            if (mTypeView!=null)
                                mTypeView.onAddFavSuccess(stringCommonResponse.getResult(),pos);
                            if (mDaliyView!=null)
                                mDaliyView.onAddFavSuccess(stringCommonResponse.getResult(),pos);
                        }else{
                            onFail(stringCommonResponse.getContent());
                        }
                    }

                    @Override
                    protected void onFail(String message) {
                        if (mTypeView!=null)
                            mTypeView.showError(message);
                        if (mDaliyView!=null)
                            mDaliyView.showError(message);
                    }
                });
    }

    @Override
    public void deleteOneFav(User user, String _id, int pos) {
        dataSource.deleteOneFav(user,_id)
                .compose(RxHelper.toUI())
                .subscribe(new RxObserver<CommonResponse<String>>(this) {
                    @Override
                    protected void onSuccess(CommonResponse<String> stringCommonResponse) {
                           if (stringCommonResponse.getResult().equals(Constant.DELETE_FAV_SUCCESS)){
                               if (mTypeView!=null)
                                   mTypeView.onDeleteFavSuccess(stringCommonResponse.getResult(),pos);
                               if (mDaliyView!=null)
                                   mDaliyView.onDeleteFavSuccess(stringCommonResponse.getResult(),pos);
                               if (mFavView!=null)
                                   mFavView.onDeleteFavSuccess(stringCommonResponse.getResult(),pos);
                           }else{
                               onFail(stringCommonResponse.getContent());
                           }
                    }
                    @Override
                    protected void onFail(String message) {
                        if (mTypeView!=null)
                            mTypeView.showError(message);
                        if (mDaliyView!=null)
                            mDaliyView.showError(message);
                        if (mFavView!=null)
                            mFavView.showError(message);
                    }
                });
    }

    @Override
    public void getFavs(User user,String type,String page,String count,boolean isLoadMore) {
        if (!isLoadMore)
            mFavView.showDialog();
        dataSource.getFavs(user,type,page,count)
                .compose(RxHelper.toUI())
                .subscribe(new RxObserver<GankFavs>(this) {
                    @Override
                    protected void onSuccess(GankFavs gankFavs) {
                        if (!gankFavs.isError())
                            mFavView.setUpFavs(gankFavs.getResult(),isLoadMore);
                        else
                            onFail(gankFavs.getMessage());
                        mFavView.hideDialog();
                    }

                    @Override
                    protected void onFail(String message) {
                        mFavView.showError(message);
                        mFavView.hideDialog();
                    }
                });
    }


    @Override
    public void getSearchResult(Context context,String word,boolean isLoadMore) {
        if (!isLoadMore)
            mSearchView.showDialog();
        if (isLoadMore){
            mSearchView.setUpSearchResult(word,null,true);
            return;
        }
         dataSource.getSearchResult(word)
                .compose(RxHelper.toUI())
                .subscribe(new RxObserver<CommonResponse<List<GankSearchItem>>>(this) {
                    @Override
                    protected void onSuccess(CommonResponse<List<GankSearchItem>> listCommonResponse) {
                        switch (listCommonResponse.getResult()){
                            case Constant.GET_SEARCH_DATA_SUCCESS:
                                mSearchView.setUpSearchResult(word,listCommonResponse.getContent(),isLoadMore);
                                break;
                            case Constant.GET_SEARCH_DATA_ERROR:
                                onFail(Constant.GET_SEARCH_DATA_ERROR);
                                break;
                            case Constant.NO_MORE_DATA:
                                onFail(Constant.NO_MORE_DATA);
                                break;
                        }
                        mSearchView.hideDialog();
                    }
                    @Override
                    protected void onFail(String message) {
                        mSearchView.showError(message);
                        mSearchView.hideDialog();
                    }
                });
    }


    @Override
    public void addOneHis(GankContent content, String token) {
        if (token==null ||token.isEmpty()) return;
        else{
            dataSource.addOneHis(content,token).compose(RxHelper.toUI())
                    .subscribe(new RxObserver<Void>(this) {
                        @Override
                        protected void onSuccess(Void aVoid) {

                        }

                        @Override
                        protected void onFail(String message) {
                            Log.d(TAG,message);
                        }
                    });
        }
    }

    @Override
    public void getCustomData(User user,boolean refresh) {
        mRecommendView.showDialog();
        dataSource.getCustomData(user).compose(RxHelper.toUI())
                .subscribe(new RxObserver<GankCustom>(this) {
                    @Override
                    protected void onSuccess(GankCustom gankCustom) {
                        setUpRecommendData(gankCustom,refresh);
                    }

                    @Override
                    protected void onFail(String message) {
                        mRecommendView.hideDialog();
                        mRecommendView.showError(message);
                    }
                });
    }

    @Override
    public void getCustomRandomData(List<Tag> tags,boolean refresh) {
        mRecommendView.showDialog();
        dataSource.getCustomRandomData(tags).compose(RxHelper.toUI())
                .subscribe(new RxObserver<GankCustom>(this) {
                    @Override
                    protected void onSuccess(GankCustom gankCustom) {
                        setUpRecommendData(gankCustom,refresh);
                    }

                    @Override
                    protected void onFail(String message) {
                        mRecommendView.hideDialog();
                        mRecommendView.showError(message);
                    }
                });
    }



    //进行推荐数据的处理
    private void setUpRecommendData(GankCustom gankCustom,boolean refresh){
        if (!gankCustom.isError()){
            List<MultiItemEntity> datas = new ArrayList<>();
            RecommendPhoto recommendPhoto = new RecommendPhoto();
            recommendPhoto.setNine_photos(gankCustom.getPhotos());
            datas.addAll(gankCustom.getItemList());
            datas.add(recommendPhoto);
            Collections.shuffle(datas);
            if (refresh) {
                datas.add(new GankSection(Constant.SECTION, "上次为阅读到这里,点击刷新"));
            }

            LogUtil.d(TAG,recommendPhoto.toString());
            mRecommendView.setRecommendData(datas, refresh);
        } else {
            mRecommendView.showError(Constant.GET_DATA_FAILED);
        }
        mRecommendView.hideDialog();
    }


    @Override
    public void getRemoteTags() {
        dataSource.getRemoteTags().compose(RxHelper.toUI())
                .subscribe(new RxObserver<CommonResponse<List<Tag>>>(this) {
                    @Override
                    protected void onSuccess(CommonResponse<List<Tag>> listCommonResponse) {
                        if (listCommonResponse!=null){
                            if (listCommonResponse.getResult().equals(Constant.GET_TAGS_SUCCESS)){
                                mRecommendView.setRecommendTagsData(listCommonResponse.getContent());
                            }else
                                onFail(listCommonResponse.getResult());
                        }

                    }

                    @Override
                    protected void onFail(String message) {
                        mRecommendView.showError(message);
                    }
                });

    }


    @Override
    public void updateUserTags(List<Tag> tags,String userToken){
        dataSource.addUserTags(tags,userToken).compose(RxHelper.toUI())
                .subscribe(new RxObserver<CommonResponse<String>>(this) {
                    @Override
                    protected void onSuccess(CommonResponse<String> stringCommonResponse) {
                        if (stringCommonResponse.getResult().equals(Constant.UPDATE_TAGS_SUCCESS)){
                            mSyncView.dataSyncSuccess(true);
                        }else
                            mSyncView.showError(stringCommonResponse.getResult());
                    }

                    @Override
                    protected void onFail(String message) {
                        mSyncView.showError(message);
                    }
                });
    }
}

