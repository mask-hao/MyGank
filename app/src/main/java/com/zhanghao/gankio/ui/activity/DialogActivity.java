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
import com.zhanghao.gankio.util.PermissionListener;
import com.zhanghao.gankio.util.ServiceUtil;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhanghao on 2017/6/10.
 */

public class DialogActivity extends BaseActivity {
    private String url;
    private String changeLog;
    private long size;
    private AlertDialog alertDialogForUpdate;
    private static final String TAG = "DialogActivity";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        BaseActivity.requestRunTimePermissions(BaseActivity.COMMON_PERMISSIONS, new PermissionListener() {
            @Override
            public void onGranted() {
                ServiceUtil.startFirRemoteService(DialogActivity.this, Constant.DOWNLOAD_NEW_APK, url, size);
                finish();
            }

            @Override
            public void onDenied(List<String> deniedPermissions) {
                String s = "缺少：";
                for (String deniedPermission : deniedPermissions) {
                    s += deniedPermission;
                }
                s += "等权限,无法下载文件";
                Toast.makeText(DialogActivity.this,s,Toast.LENGTH_LONG).show();
                finish();
            }
        });

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
    }


}
