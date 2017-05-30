package com.zhanghao.gankio.presenter;
import com.zhanghao.gankio.entity.GankContent;
import com.zhanghao.gankio.entity.User;

/**
 * Created by zhanghao on 2017/5/3.
 */

public interface BaseGankPresenter  extends BasePresenter{
    void addOneFav(GankContent content,String token,int pos);
    void deleteOneFav(User user, String _id, int pos);
}
