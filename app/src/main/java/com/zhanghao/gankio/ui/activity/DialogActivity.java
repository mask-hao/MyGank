package com.zhanghao.gankio.ui.activity;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;
import com.zhanghao.gankio.R;
import com.zhanghao.gankio.entity.Constant;
import com.zhanghao.gankio.util.ActivityPool;
import com.zhanghao.gankio.util.ServiceUtil;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhanghao on 2017/6/10.
 */

public class DialogActivity extends AppCompatActivity {
    private static final int REQUEST_PERMISSION = 102;
    private String url;
    private String changeLog;
    private long size;
    private AlertDialog alertDialogForUpdate;
    private static final String TAG = "DialogActivity";
    private  String [] FILE_PERMISSIONS={Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE};
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityPool.addActivity(this);
        initData();
        initDialog();
    }

    private void initDialog() {
        alertDialogForUpdate = new AlertDialog.Builder(DialogActivity.this)
                .setCancelable(false)
                .setTitle("发现新版本")
                .setMessage(changeLog)
                .setNegativeButton(R.string.nextTime, (dialog, which) -> {
                    alertDialogForUpdate.dismiss();
                    finish();
                })
                .setPositiveButton(R.string.sure, ((dialog, which) -> {
                    alertDialogForUpdate.dismiss();
                    requestFilePermission();
                }))
                .show();
    }

    private void requestFilePermission() {
        List<String> permissionList = new ArrayList<>();
        for (String permission : FILE_PERMISSIONS){
            if (ContextCompat.checkSelfPermission(this,permission) != PackageManager.PERMISSION_GRANTED){
                permissionList.add(permission);
            }
        }
        if(!permissionList.isEmpty()){
            ActivityCompat.requestPermissions(this,permissionList.toArray(new String[permissionList.size()]),REQUEST_PERMISSION);
        }else{
            ServiceUtil.startFirRemoteService(this, Constant.DOWNLOAD_NEW_APK, url, size);
            finish();
        }


    }


    private void initData() {
        Intent intent = getIntent();
        url = intent.getStringExtra("url");
        changeLog = intent.getStringExtra("changeLog");
        size = intent.getLongExtra("size",0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityPool.removeActivity(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_PERMISSION:
                if (grantResults.length>0){
                    List<String> deniedPermissions=new ArrayList<>();
                    for (int i=0;i<grantResults.length;i++){
                        int grantResult=grantResults[i];
                        String permission=permissions[i];
                        if (grantResult==PackageManager.PERMISSION_DENIED){
                            deniedPermissions.add(permission);
                        }
                    }
                    if (deniedPermissions.isEmpty())
                        ServiceUtil.startFirRemoteService(this, Constant.DOWNLOAD_NEW_APK, url, size);
                    else
                        showDeniedPermissions(permissions);
                }
                break;
        }
        finish();
    }

    private void showDeniedPermissions(String[] permissions) {
                            String s = "缺少：";
                            for (String deniedPermission : permissions) {
                                s += deniedPermission;
                            }
                            s += "等权限,无法下载文件";
        Toast.makeText(this,s,Toast.LENGTH_LONG).show();

    }


}
