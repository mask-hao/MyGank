package com.zhanghao.gankio.ui.activity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.zhanghao.gankio.R;
import com.zhanghao.gankio.service.SplashImageService;
import com.zhanghao.gankio.util.ActivityUtil;
import com.zhanghao.gankio.util.LogUtil;
import com.zhanghao.gankio.util.ServiceUtil;
import com.zhanghao.gankio.util.SharedPrefsUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zhanghao on 2017/5/8.
 */

public class SplashActivity extends AppCompatActivity {
    private static final String TAG = "SplashActivity";
    @BindView(R.id.splash_iv)
    ImageView splashIv;
    private String defaultUrl="https://images.unsplash.com/photo-1494122474412-aeaf73d11da8?w=1080&h=1920";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideSystemUI();
        setContentView(R.layout.splash_activity);
        ButterKnife.bind(this);
        initData();
        initView();
    }

    private void initData() {
        String url=getImgUrl();
        if (url==null||url.isEmpty()){
            SharedPrefsUtils.setStringPreference(this,"start-img",defaultUrl);
        }
    }

    private String getImgUrl(){
        return  SharedPrefsUtils.getStringPreference(this,"start-img");
    }


    private void initView() {
        Glide.with(this).load(getImgUrl()).diskCacheStrategy(DiskCacheStrategy.ALL).into(splashIv);
        ObjectAnimator.ofFloat(splashIv,"alpha",0.2f,1.0f).setDuration(2000).start();
        splashIv.postDelayed(() -> {
            ActivityUtil.gotoMainActivity(this);
            finish();
        }, 2000);
        ServiceUtil.startSplashImgService(this);
    }



    private void hideSystemUI() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
