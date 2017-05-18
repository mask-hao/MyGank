package com.zhanghao.gankio.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import com.zhanghao.gankio.BuildConfig;
import com.zhanghao.gankio.R;
import com.zhanghao.gankio.contract.UserContract;
import com.zhanghao.gankio.entity.Constant;
import com.zhanghao.gankio.entity.User;
import com.zhanghao.gankio.listener.NewApkListener;
import com.zhanghao.gankio.listener.NewVersionListener;
import com.zhanghao.gankio.presenter.UserPresenter;
import com.zhanghao.gankio.service.FirRemoteService;
import com.zhanghao.gankio.util.ActivityPool;
import com.zhanghao.gankio.util.ActivityUtil;
import com.zhanghao.gankio.util.FileUtil;
import com.zhanghao.gankio.util.PermissionListener;
import com.zhanghao.gankio.util.ServiceUtil;
import com.zhanghao.gankio.util.SharedPrefsUtils;
import com.zhanghao.gankio.util.UserUtil;

import java.util.List;

import butterknife.BindView;

/**
 * Created by zhanghao on 2017/4/28.
 */

public class SettingActivity extends BaseActivity implements UserContract.LogoutView, NewApkListener, NewVersionListener {
    private static final String TAG = "SettingActivity";
    @BindView(R.id.setting_sw)
    Switch settingSw;
    @BindView(R.id.setting_wifi_img_ll)
    LinearLayout settingWifiImgLl;
    @BindView(R.id.setting_logout_ll)
    LinearLayout settingLogoutLl;
    AlertDialog alertDialog;
    @BindView(R.id.setting_clearCache_ll)
    LinearLayout settingClearCacheLl;
    @BindView(R.id.setting_cacheSize_ll)
    TextView settingCacheSizeLl;
    @BindView(R.id.setting_version_ll)
    TextView settingVersionLl;
    @BindView(R.id.setting_checkUpdate_ll)
    LinearLayout settingCheckUpdateLl;
    @BindView(R.id.setting_about_ll)
    LinearLayout settingAboutLl;

    @Override
    protected int setContentLayout() {
        return R.layout.setting;
    }

    @Override
    protected boolean canBack() {
        return true;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.setting_tv);
        initView();
    }

    private void initView() {
        int buildVersionCode = BuildConfig.VERSION_CODE;
        String buildVersionName = BuildConfig.VERSION_NAME;
        settingVersionLl.setText("当前版本" + buildVersionName + "  ( " + buildVersionCode + " )");
        settingCacheSizeLl.setText("包括图片，网页等缓存，共" + "(" + FileUtil.calculateCacheSize(this) + ")");
        boolean wifi_only = SharedPrefsUtils.getBooleanPreference(this, Constant.WIFI_ONLY, false);
        settingSw.setChecked(wifi_only);
        settingWifiImgLl.setOnClickListener(v -> {
            initWifiSwitch();
        });
        settingLogoutLl.setOnClickListener(v -> {
            showLogoutDialog();
        });
        settingClearCacheLl.setOnClickListener(v -> {
            showClearCacheDialog();
        });
        settingCheckUpdateLl.setOnClickListener(v->{
            checkUpdate();
        });
    }

    private void checkUpdate() {
        ServiceUtil.startFirRemoteService(this,Constant.GET_APP_INFO);
        FirRemoteService.setNewApkListener(this);
        FirRemoteService.setNewVersionListener(this);
    }

    private void initWifiSwitch() {
        boolean saved = SharedPrefsUtils.getBooleanPreference(this, Constant.WIFI_ONLY, false);
        if (saved) {
            settingSw.setChecked(false);
            SharedPrefsUtils.setBooleanPreference(this, Constant.WIFI_ONLY, false);
        } else {
            settingSw.setChecked(true);
            SharedPrefsUtils.setBooleanPreference(this, Constant.WIFI_ONLY, true);
        }
    }


    private void showClearCacheDialog() {
        alertDialog = new AlertDialog.Builder(this).setCancelable(true)
                .setMessage(R.string.doclearCache)
                .setNegativeButton(R.string.cancle, (dialog, which) -> {
                    alertDialog.dismiss();
                })
                .setPositiveButton(R.string.sure, (dialog, which) -> {
                    if (FileUtil.clearCache(this)) {
                        Toast.makeText(this, R.string.clearCacheSuccess, Toast.LENGTH_SHORT).show();
                        settingCacheSizeLl.setText(R.string.cacheInitSize);
                    } else {
                        Toast.makeText(this, R.string.clearCacheError, Toast.LENGTH_SHORT).show();
                    }
                })
                .show();
    }

    private void showLogoutDialog() {
        alertDialog = new AlertDialog.Builder(this).setMessage(R.string.dologout)
                .setCancelable(true)
                .setPositiveButton(R.string.sure, (dialog, which) -> {
                    if (!User.getInstance().getUserToken().isEmpty())
                        new UserPresenter(this).logout(User.getInstance());
                    else
                        alertDialog.dismiss();
                })
                .setNegativeButton(R.string.cancle, ((dialog, which) -> {
                    alertDialog.dismiss();
                }))
                .show();
    }


    @Override
    public void showDialog() {

    }

    @Override
    public void hideDialog() {

    }

    @Override
    public void showError(String message) {

    }

    @Override
    public void setPresenter(UserContract.Presenter presenter) {

    }

    @Override
    public void logoutResult(String message) {
        UserUtil.clearUserInfo(this);
        ActivityPool.finishAll();
        ActivityUtil.gotoMainActivity(this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void findNewApk(String updateInfo, String downloadUrl, long size) {
        requestRunTimePermissions(COMMON_PERMISSIONS, new PermissionListener() {
            @Override
            public void onGranted() {
                alertDialog=new AlertDialog.Builder(SettingActivity.this)
                        .setCancelable(true)
                        .setTitle("发现新版本")
                        .setMessage(updateInfo)
                        .setNegativeButton(R.string.nextTime, (dialog, which) -> {
                            alertDialog.dismiss();
                        })
                        .setPositiveButton(R.string.sure,((dialog, which) -> {
                            ServiceUtil.startFirRemoteService(SettingActivity.this,Constant.DOWNLOAD_NEW_APK,downloadUrl,size);
                            alertDialog.dismiss();
                        }))
                        .show();
            }

            @Override
            public void onDenied(List<String> deniedPermissions) {
                String s="缺少：";
                for (String deniedPermission : deniedPermissions) {
                    s+=deniedPermission;
                }
                s+="等权限";

                alertDialog=new AlertDialog.Builder(SettingActivity.this)
                        .setCancelable(true)
                        .setTitle("缺少权限")
                        .setMessage(s)
                        .setNegativeButton(R.string.cancle, (dialog, which) -> {
                            alertDialog.dismiss();
                        })
                        .setPositiveButton(R.string.goSetting,((dialog, which) -> {
                            ActivityUtil.gotoSetting(SettingActivity.this);
                            alertDialog.dismiss();
                        }))
                        .show();
            }
        });
    }

    @Override
    public void showToast() {
        Toast.makeText(this,"已经安装最新版本",Toast.LENGTH_SHORT).show();
    }
}
