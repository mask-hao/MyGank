package com.zhanghao.gankio.ui.activity;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.view.MenuItem;
import android.widget.Toast;

import com.zhanghao.gankio.R;
import com.zhanghao.gankio.entity.Constant;
import com.zhanghao.gankio.listener.HomeFrgListener;
import com.zhanghao.gankio.listener.NewApkListener;
import com.zhanghao.gankio.service.FirRemoteService;
import com.zhanghao.gankio.ui.widget.MyBottomNavigationView;
import com.zhanghao.gankio.util.ActivityUtil;
import com.zhanghao.gankio.util.FragmentUtil;
import com.zhanghao.gankio.util.PermissionListener;
import com.zhanghao.gankio.util.ServiceUtil;
import com.zhanghao.gankio.util.UserUtil;
import java.util.List;


public class MainActivity extends BaseActivity implements HomeFrgListener {

    private static final String TAG = "MainActivity";
    MyBottomNavigationView navigation;
    private FragmentUtil fragmentUtil;
    private AlertDialog alertDialog;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fragmentUtil.initFragment(Constant.HOME);
                    return true;
                case R.id.navigation_more:
                    fragmentUtil.initFragment(Constant.MORE);
                    return true;
                case R.id.navigation_user:
                    fragmentUtil.initFragment(Constant.ME);
                    return true;
            }
            return false;
        }

    };


    @Override
    protected int setContentLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected boolean canBack() {
        return false;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initPermission();
        UserUtil.initSerializedUser();
        initOtherService();
    }

    private void initPermission() {
        requestRunTimePermissions(new String[]{Manifest.permission.READ_PHONE_STATE}, new PermissionListener() {
            @Override
            public void onGranted() {}
            @Override
            public void onDenied(List<String> deniedPermissions) {
                Toast.makeText(MainActivity.this,"缺少"+deniedPermissions.get(0)+"权限，某些功能无法使用",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initOtherService() {
        ServiceUtil.startFirRemoteService(this,Constant.GET_APP_INFO);
    }


    private void initView() {
        fragmentUtil = new FragmentUtil(this);
        fragmentUtil.initFragment(Constant.HOME);
        navigation= (MyBottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    @Override
    public void hideToolbar() {
        if (mToolbar.isShow()){
            mToolbar.hide();
        }

    }

    @Override
    public void showToolbar() {
        if (!mToolbar.isShow()){
            mToolbar.show();
        }

    }

    @Override
    public void hideBottomBar() {
        if (!navigation.isHidden()){
            navigation.hide();
        }

    }

    @Override
    public void showBottomBar() {
        if (navigation.isHidden()){
            navigation.show();
        }
    }


    @Override
    public void setToolbarTitle(String title) {
        mToolbar.setTitle(title);
    }


}
