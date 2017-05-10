package com.zhanghao.gankio.ui.activity;
import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import com.zhanghao.gankio.R;
import com.zhanghao.gankio.presenter.BasePresenter;
import com.zhanghao.gankio.ui.widget.MyToolbar;
import com.zhanghao.gankio.util.ActivityPool;
import com.zhanghao.gankio.util.PermissionListener;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import skin.support.app.SkinCompatActivity;

/**
 * Created by zhanghao on 2016/11/20.
 */

public abstract class BaseActivity<P extends BasePresenter> extends SkinCompatActivity{

    protected P mPresenter;


    private static final String TAG = "BaseActivity";
    private static final int REQUEST_PERMISSION=101;
    abstract protected int setContentLayout();
    abstract protected boolean canBack();
    private static PermissionListener permissionListener;
    protected MyToolbar mToolbar;
    public static String [] COMMON_PERMISSIONS={Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityPool.addActivity(this);
        setContentView(setContentLayout());
        ButterKnife.bind(this);
        mToolbar=(MyToolbar) findViewById(R.id.toolbar);
        if (mToolbar==null) throw new IllegalStateException("the BaseActivity must be contain a toolbar");
        setUpToolBar();
    }

    protected void setUpToolBar(){
        setSupportActionBar(mToolbar);
        if (canBack()){
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        switch (id){
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    /**
     * 动态申请权限
     * @param permissions 所有的权限
     */
    public static void requestRunTimePermissions(String [] permissions, PermissionListener listener){
        permissionListener=listener;
        Activity activity= ActivityPool.getTopActivity();
        if (activity==null)
            return;
        else{
            List<String> permissionList=new ArrayList<>();
            for (String permission:permissions){
                if (ContextCompat.checkSelfPermission(activity,permission)!= PackageManager.PERMISSION_GRANTED)
                    permissionList.add(permission);
            }
            if (!permissionList.isEmpty()){
                ActivityCompat.requestPermissions(activity,permissionList.toArray(new String[permissionList.size()]),REQUEST_PERMISSION);
            }else
                permissionListener.onGranted();
        }
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
                        permissionListener.onGranted();
                    else
                        permissionListener.onDenied(deniedPermissions);
                }
                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mPresenter!=null)
            mPresenter.unSubscribe();
        ActivityPool.removeActivity(this);
    }

}
