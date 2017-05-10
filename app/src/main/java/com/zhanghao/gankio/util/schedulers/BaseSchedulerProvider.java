package com.zhanghao.gankio.util.schedulers;

import android.support.annotation.Nullable;

import io.reactivex.Scheduler;

/**
 * Created by zhanghao on 2017/4/20.
 */

public interface BaseSchedulerProvider {
    @Nullable
    Scheduler io();

    @Nullable
    Scheduler ui();
}
