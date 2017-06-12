package com.zhanghao.gankio.rx;

import com.zhanghao.gankio.presenter.BasePresenterImpl;
import com.zhanghao.gankio.util.LogUtil;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by zhanghao on 2017/4/22.
 */

public  abstract class RxObserver<T> implements Observer<T> {
    private static final String TAG = "RxObserver";

    private BasePresenterImpl presenter;


    public RxObserver(BasePresenterImpl presenter){
        this.presenter=presenter;
    }

    @Override
    public void onSubscribe(Disposable d) {
        presenter.addDisposable(d);
        LogUtil.i(TAG,this.presenter.getClass().getSimpleName());
    }

    @Override
    public void onError(Throwable t) {
        if (t!=null && t.getMessage()!=null){
            LogUtil.d(TAG,t.getMessage());
            t.printStackTrace();
        }
        onFail(RxException.getErrorMessage(t));
    }

    @Override
    public void onComplete() {
    }

    @Override
    public void onNext(T t) {
        onSuccess(t);
    }

    protected abstract void onSuccess(T t);
    protected abstract void onFail(String message);



}
