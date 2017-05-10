package com.zhanghao.gankio;

import android.app.Application;
import android.content.Context;

import im.fir.sdk.FIR;
import skin.support.SkinCompatManager;
import skin.support.app.SkinCardViewInflater;
import skin.support.design.app.SkinMaterialViewInflater;

/**
 * Created by zhanghao on 2016/11/24.
 */

public class BaseApplication extends Application{


    private static BaseApplication baseApplication;


    @Override
    public void onCreate() {
        super.onCreate();
        FIR.init(this);
        SkinCompatManager.init(this)
                .addInflater(new SkinCardViewInflater())
                .addInflater(new SkinMaterialViewInflater())
                .loadSkin();
        baseApplication=this;
    }

    public static Context getContext(){
        return baseApplication;
    }

}
