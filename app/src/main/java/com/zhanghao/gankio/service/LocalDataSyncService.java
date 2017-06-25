package com.zhanghao.gankio.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.zhanghao.gankio.R;
import com.zhanghao.gankio.contract.GankContract;
import com.zhanghao.gankio.entity.User;
import com.zhanghao.gankio.model.GankDataLocalRepository;
import com.zhanghao.gankio.model.GankDataRemoteRepository;
import com.zhanghao.gankio.presenter.GankDataSyncPresenter;
import com.zhanghao.gankio.util.LogUtil;
import com.zhanghao.gankio.util.ServiceUtil;
import com.zhanghao.gankio.util.SharedPrefsUtils;

/**
 * Created by zhanghao on 2017/6/24.
 */

public class LocalDataSyncService extends Service implements GankContract.RecommendDataSyncView{


    private static final String TAG = "LocalDataSyncService";
    private GankDataSyncPresenter syncPresenter;
    private int repeatCount = 0;


    @Override
    public void onCreate() {
        super.onCreate();
         syncPresenter = new GankDataSyncPresenter(GankDataLocalRepository.getInstance(),
                GankDataRemoteRepository.getInstance(),
                this);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: 执行了一次");
        syncPresenter.updateUserTags(this, User.getInstance().getUserToken());
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }

    @Override
    public void showDialog() {

    }

    @Override
    public void hideDialog() {

    }

    @Override
    public void showError(String message) {
        SharedPrefsUtils.setBooleanPreference(this,getString(R.string.syncData),false);
        if (repeatCount<3){
            ServiceUtil.startSyncDataService(this);
            repeatCount++;
        }

    }

    @Override
    public void setPresenter(GankContract.RecommendPresenter presenter) {

    }

    @Override
    public void dataSyncSuccess(boolean sync) {

        LogUtil.d(TAG,String.valueOf(sync));
        if (sync){
            SharedPrefsUtils.setBooleanPreference(this,getString(R.string.syncData),true);
            stopSelf();
        }
    }
}
