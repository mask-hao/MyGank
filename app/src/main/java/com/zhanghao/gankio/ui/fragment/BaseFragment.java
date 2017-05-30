package com.zhanghao.gankio.ui.fragment;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhanghao.gankio.presenter.BasePresenter;

/**
 * Created by zhanghao on 2017/4/15.
 */

public abstract class BaseFragment<P extends BasePresenter> extends Fragment{

    protected P mPresenter;

    View root;

    public abstract int getLayoutId();



    View getRoot(LayoutInflater inflater, @Nullable ViewGroup container){
        return inflater.inflate(getLayoutId(),container,false);
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter!=null)
            mPresenter.unSubscribe();
    }
}
