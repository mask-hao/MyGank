package com.zhanghao.gankio.ui.activity;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.zhanghao.gankio.R;
import com.zhanghao.gankio.entity.enumconstant.BottomConstant;
import com.zhanghao.gankio.entity.Constant;
import com.zhanghao.gankio.listener.HomeFrgListener;
import com.zhanghao.gankio.ui.widget.MyBottomNavigationView;
import com.zhanghao.gankio.util.FragmentUtil;
import com.zhanghao.gankio.util.LogUtil;
import com.zhanghao.gankio.util.PermissionListener;
import com.zhanghao.gankio.util.ServiceUtil;
import com.zhanghao.gankio.util.SharedPrefsUtils;
import com.zhanghao.gankio.util.UserUtil;
import java.util.List;


public class MainActivity extends BaseToolbarActivity implements HomeFrgListener {

    private static final String TAG = "MainActivity";
    MyBottomNavigationView navigation;
    private FragmentUtil fragmentUtil;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_recommend:
                    fragmentUtil.initFragment(BottomConstant.RECOMMEND);
                    return true;
                case R.id.navigation_everyday:
                    fragmentUtil.initFragment(BottomConstant.DAY);
                    return true;
                case R.id.navigation_more:
                    fragmentUtil.initFragment(BottomConstant.MORE);
                    return true;
                case R.id.navigation_user:
                    fragmentUtil.initFragment(BottomConstant.ME);
                    return true;
            }
            return true;
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
        if (!SharedPrefsUtils.getBooleanPreference(this,getString(R.string.syncData),false)){
            ServiceUtil.startSyncDataService(this);
        }
    }


    private void initView() {
        navigation= (MyBottomNavigationView) findViewById(R.id.navigation);
        initNavigationMenu();
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


    }

    private void initNavigationMenu() {
        fragmentUtil = new FragmentUtil(this);
        if (!SharedPrefsUtils.getBooleanPreference(this, getString(R.string.homePage_setting_tv), false)) {
            navigation.inflateMenu(R.menu.navigation_false);
            fragmentUtil.initFragment(BottomConstant.RECOMMEND);
        } else {
            navigation.inflateMenu(R.menu.navigation_true);
            fragmentUtil.initFragment(BottomConstant.DAY);
        }
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


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
