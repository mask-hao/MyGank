package com.zhanghao.gankio.presenter;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * 
 * Created by zhanghao on 2017/4/20.
 */

public class BasePresenterImpl implements BasePresenter{

    private static final String TAG = "BasePresenterImpl";

    private CompositeDisposable compositeDisposable;


    public BasePresenterImpl() {
        this.compositeDisposable = new CompositeDisposable();
    }


    public void addDisposable(Disposable disposable){
        if(disposable == null)
            throw new NullPointerException("disposable can't be null");
        else
            this.compositeDisposable.add(disposable);
    }



    public void addDisposable(Disposable... disposables){
        for (int i = 0; i < disposables.length; i++) {
            if (disposables[i]==null)
                throw new NullPointerException("the No"+i+" is null");
        }
        this.compositeDisposable.addAll(disposables);
    }






    @Override
    public String toString() {
        return "size: " +compositeDisposable.size()+compositeDisposable;
    }

    @Override
    public void unSubscribe() {
        this.compositeDisposable.clear();
    }
}
