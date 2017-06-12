package com.zhanghao.gankio.service;


import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.zhanghao.gankio.BaseApplication;
import com.zhanghao.gankio.BuildConfig;
import com.zhanghao.gankio.R;
import com.zhanghao.gankio.entity.AppInfo;
import com.zhanghao.gankio.entity.Constant;
import com.zhanghao.gankio.entity.NewApk;
import com.zhanghao.gankio.listener.NewApkListener;
import com.zhanghao.gankio.listener.NewVersionListener;
import com.zhanghao.gankio.model.FirDataRepository;
import com.zhanghao.gankio.model.FirDataSource;
import com.zhanghao.gankio.ui.activity.MainActivity;
import com.zhanghao.gankio.util.ActivityPool;
import com.zhanghao.gankio.util.ActivityUtil;
import com.zhanghao.gankio.util.LogUtil;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by zhanghao on 2017/5/9.
 */

public class FirRemoteService extends IntentService{
    private static final String TAG = "FirRemoteService";
    private FirDataSource dataSource;
    private NotificationCompat.Builder mBuilder;
    private NotificationManager mManager;
    private int totalFileSize;
//    private static NewApkListener mNewApkListener;
//    private static NewVersionListener newVersionListener;
//
//    public static void setNewVersionListener(NewVersionListener newVersionListener) {
//        FirRemoteService.newVersionListener = newVersionListener;
//    }
//
//    public static void setNewApkListener(NewApkListener mNewApkListener) {
//        FirRemoteService.mNewApkListener = mNewApkListener;
//    }

    public FirRemoteService() {
        super("");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        dataSource= FirDataRepository.getInstance();
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        int code=intent.getIntExtra(Constant.FIR_CODE,0);
        switch (code){
            case Constant.GET_APP_INFO:
                showNewApp();
                break;
            case Constant.DOWNLOAD_NEW_APK:
                String url=intent.getStringExtra(Constant.FIR_DOWNLOAD_URL);
                long size=intent.getLongExtra(Constant.FIR_NEW_APK_SIZE,0);
                if (size == 0)
                    break;
                downloadNewApk(url,size);
                break;
            case 0:
               break;
        }
    }

//    --------------------------------------------------------

    private void downloadNewApk(String url, long size) {
        mManager= (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder=new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("下载")
                .setContentText("正在下载更新...")
                .setAutoCancel(true);
        mManager.notify(0,mBuilder.build());
        beignDownLoad(url,size);
    }

    private void beignDownLoad(String url, long size) {
        Call<ResponseBody> call=dataSource.downloadApk(url);
        ResponseBody responseBody=null;
        try {
            responseBody=call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        int count;
        byte [] data = new byte[1024*4];
        long fileSize=size;
        InputStream inputStream=new BufferedInputStream(responseBody.byteStream(),1024*8);
        File outputFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "GankIo.apk");

        try {
            OutputStream outputStream=new FileOutputStream(outputFile);
            long total = 0;
            long startTime = System.currentTimeMillis();
            int timeCount=1;
            while ((count=inputStream.read(data))!=-1){
                total+=count;
                totalFileSize = (int) (fileSize / (Math.pow(1024, 2)));
                double current = Math.round(total / (Math.pow(1024, 2)));
                int progress = (int) ((total * 100) / fileSize);
                long currentTime = System.currentTimeMillis() - startTime;
                NewApk apk=new NewApk();
                apk.setTotalFileSize(totalFileSize);

                if (currentTime > 1000 * timeCount) {
                    apk.setCurrentFileSize((int) current);
                    apk.setProgress(progress);
                    sendNotification(apk);
                    timeCount++;
                }
                outputStream.write(data, 0, count);
            }
            onDownLoadComplete();
            outputStream.flush();
            outputStream.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void onDownLoadComplete() {
        NewApk apk=new NewApk();
        apk.setProgress(100);
        mManager.cancel(0);
        mBuilder.setProgress(0,0,false);
        mBuilder.setContentText("文件下载完成！");
        Intent intent = new Intent(Intent.ACTION_VIEW);
        File downloadFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "GankIo.apk");

        Uri fileUri;

        if (Build.VERSION.SDK_INT>=24){
            fileUri = FileProvider.getUriForFile(this, "com.zhanghao.gankio.fileprovider", downloadFile);
        }else
            fileUri = Uri.fromFile(downloadFile);

        intent.setDataAndType(fileUri, "application/vnd.android.package-archive");

        intent.setFlags(
                          Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_GRANT_READ_URI_PERMISSION
                        | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        mBuilder.setContentIntent(pendingIntent);
        mManager.notify(0, mBuilder.build());
        mManager.cancel(0);
        startActivity(intent);
        stopSelf();
    }

    private void sendNotification(NewApk apk) {
        mBuilder.setProgress(100,apk.getProgress(),false);
        mBuilder.setContentText("已下载："+apk.getProgress() + "%" + " (" + apk.getTotalFileSize() + "MB" + ")");
        mManager.notify(0,mBuilder.build());
    }


    //    ---------------------------------------------------------------------------
    private void showNewApp(){
        AppInfo appInfo = getAppInfo();
        if (appInfo==null)
            return;
        int remoteVersionCode=appInfo.getBuild();
        int localVersionCode=BuildConfig.VERSION_CODE;
        if (remoteVersionCode>localVersionCode){
            showNewAppDialog(appInfo.getChangelog(),appInfo.getDirect_install_url(),appInfo.getBinary().getFsize());
        }else{
            new Handler(getMainLooper()).post(()->{
                if (! (ActivityPool.getTopActivity() instanceof MainActivity))
                    Toast.makeText(BaseApplication.getContext(),"已经是最新版本",Toast.LENGTH_SHORT).show();
            });
        }
    }

    private void showNewAppDialog(String changeLog,String url,long size) {
       
        new Handler(getMainLooper()).post(()->{
            ActivityUtil.gotoDialogActivity(BaseApplication.getContext(),changeLog,url,size);
        });

    }

    private AppInfo getAppInfo() {
       Call<AppInfo> call = dataSource.getAppInfo();
        try {
            Response<AppInfo> appInfoResponse=call.execute();
            if (appInfoResponse.isSuccessful()){
                LogUtil.d(TAG,appInfoResponse.body().toString());
                 return appInfoResponse.body();
            }else{
                LogUtil.d(TAG,"获取不成功");
                return null;
            }

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
