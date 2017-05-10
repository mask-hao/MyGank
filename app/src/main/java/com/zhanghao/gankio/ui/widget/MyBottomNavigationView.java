package com.zhanghao.gankio.ui.widget;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.design.widget.BottomNavigationView;
import android.util.AttributeSet;

import skin.support.widget.SkinCompatBackgroundHelper;
import skin.support.widget.SkinCompatSupportable;

/**
 * Created by zhanghao on 2017/4/27.
 */

public class MyBottomNavigationView extends BottomNavigationView implements SkinCompatSupportable {
    private Context mContext;
    private ObjectAnimator mHideAnim; // 隐藏的动画
    private ObjectAnimator mShowAnim; // 显现的动画
    private boolean mIsHidden; // 是否已隐藏
    private float mPxHeight; // BottomNavigationView 的 px 高度
    private SkinCompatBackgroundHelper backgroundHelper;

    public MyBottomNavigationView(Context context) {
        this(context,null);
    }

    public MyBottomNavigationView(Context context, AttributeSet attr) {
        this(context, attr,0);

    }


    public MyBottomNavigationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
        backgroundHelper=new SkinCompatBackgroundHelper(this);
        backgroundHelper.loadFromAttributes(attrs,defStyleAttr);
    }

    private void init() {
        mIsHidden = false;
        mPxHeight = dp2px(56);
        mHideAnim = ObjectAnimator.ofFloat(this, "translationY", 0, mPxHeight)
                .setDuration(300);
        mShowAnim = ObjectAnimator.ofFloat(this, "translationY", mPxHeight, 0)
                .setDuration(300);
    }

    public void hide() {
        mIsHidden = true;
        mHideAnim.start();
    }
    public void show() {
        mIsHidden = false;
        mShowAnim.start();
    }

    public boolean isHidden() {
        return mIsHidden;
    }


    private   int dp2px(float dpValue) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    @Override
    public void applySkin() {
        if (backgroundHelper!=null)
            backgroundHelper.applySkin();
    }
}
