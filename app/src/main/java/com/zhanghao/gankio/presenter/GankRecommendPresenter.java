package com.zhanghao.gankio.presenter;

import android.content.Context;

import com.zhanghao.gankio.R;
import com.zhanghao.gankio.contract.GankContract;
import com.zhanghao.gankio.entity.GankContent;
import com.zhanghao.gankio.entity.Tag;
import com.zhanghao.gankio.entity.User;
import com.zhanghao.gankio.model.GankDataSource;
import com.zhanghao.gankio.util.LogUtil;
import com.zhanghao.gankio.util.SharedPrefsUtils;

import java.util.List;

/**
 * Created by zhanghao on 2017/6/23.
 */

public class GankRecommendPresenter extends BasePresenterImpl implements
        GankContract.RecommendPresenter{
    private GankLocalPresenter localPresenter;
    private GankRemotePresenter remotePresenter;
    private static final String TAG = "GankRecommendPresenter";

    public GankRecommendPresenter(GankContract.RecommendView recommendView,
                                  GankDataSource.GankLocalDataSource localDataSource,
                                   GankDataSource.GankRemoteDataSource remoteDataSource){
        this.localPresenter = new GankLocalPresenter(localDataSource);
        this.remotePresenter = new GankRemotePresenter(
                recommendView,
                remoteDataSource,
                this);
    }

    @Override
    public void getCommonRecommend(User user, Context context,boolean refresh) {
        List<Tag> tags = getLocalTags(context);


        boolean firstLoad = (tags == null || tags.size() == 0) && ((user.getUserToken() == null || user.getUserToken().isEmpty()) || !SharedPrefsUtils.getBooleanPreference(context, context.getString(R.string.syncData), false));

        boolean userLoginButNoTags = user.getUserToken() != null && !user.getUserToken().isEmpty() && (tags == null || tags.size() == 0);

        boolean userNotLoginExistTags = (user.getUserToken() == null || user.getUserToken().isEmpty()) && tags != null && tags.size() > 0;

        boolean userLoginExistTags = user.getUserToken() != null && !user.getUserToken().isEmpty() && tags != null && tags.size()>0;


        if (firstLoad){
             LogUtil.d(TAG,"首次加载");
             remotePresenter.getRemoteTags();
            return;
        }

        if (userLoginButNoTags){
            LogUtil.d(TAG,"用户登录过但是没有选择标签");
            remotePresenter.getRemoteTags();
            return;
        }

        if (userNotLoginExistTags){
            LogUtil.d(TAG,"用户不登录，选择了标签");
            remotePresenter.getCustomRandomData(tags,refresh);
            return;
        }
        if (userLoginExistTags){
            LogUtil.d(TAG,"用户登录,有标签");
            remotePresenter.getCustomData(User.getInstance(),refresh);
        }

    }

    @Override
    public List<Tag> getLocalTags(Context context) {
        return localPresenter.getLocalTags(context);
    }


    @Override
    public void saveLocalTags(Context context,List<Tag> tags) {
        localPresenter.saveLocalTags(context,tags);
    }



    @Override
    public void addOneFav(GankContent content, String token, int pos) {
        remotePresenter.addOneFav(content,token,pos);
    }

    @Override
    public void deleteOneFav(User user, String _id, int pos) {
        remotePresenter.deleteOneFav(user, _id, pos);
    }

    @Override
    public void addOneHis(GankContent content, String token) {
        remotePresenter.addOneHis(content, token);
    }
}
