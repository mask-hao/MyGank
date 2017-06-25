package com.zhanghao.gankio.util;

import android.content.Context;
import android.content.Intent;

import com.zhanghao.gankio.entity.Constant;
import com.zhanghao.gankio.service.FirRemoteService;
import com.zhanghao.gankio.service.LocalDataSyncService;
import com.zhanghao.gankio.service.SplashImageService;

/**
 * Created by zhanghao on 2017/5/9.
 */

public class ServiceUtil {
    public static void startSplashImgService(Context context){
        context.startService(new Intent(context, SplashImageService.class));
    }

    public static void startFirRemoteService(Context context,int code){
        Intent intent=new Intent(context,FirRemoteService.class);
        intent.putExtra(Constant.FIR_CODE,code);
        context.startService(intent);
    }

    public static void startFirRemoteService(Context context,int code,String downLoadUrl,long size){
        Intent intent=new Intent(context,FirRemoteService.class);
        intent.putExtra(Constant.FIR_CODE,code);
        intent.putExtra(Constant.FIR_DOWNLOAD_URL,downLoadUrl);
        intent.putExtra(Constant.FIR_NEW_APK_SIZE,size);
        context.startService(intent);
    }


    public static void startSyncDataService(Context context){
        Intent intent = new Intent(context, LocalDataSyncService.class);
        context.startService(intent);
    }



}
