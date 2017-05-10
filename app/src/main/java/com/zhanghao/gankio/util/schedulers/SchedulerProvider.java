package com.zhanghao.gankio.util.schedulers;

import android.support.annotation.Nullable;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by zhanghao on 2017/4/20.
 */

public class SchedulerProvider implements BaseSchedulerProvider{

    private SchedulerProvider(){}

    public static SchedulerProvider getInstance(){
        return SchedulerProviderHolder.schedulerProvider;
    }

    private static class SchedulerProviderHolder{
        private static final SchedulerProvider schedulerProvider=new SchedulerProvider();
    }

    @Nullable
    @Override
    public Scheduler io() {
        return Schedulers.io();
    }

    @Nullable
    @Override
    public Scheduler ui() {
        return AndroidSchedulers.mainThread();
    }
}
