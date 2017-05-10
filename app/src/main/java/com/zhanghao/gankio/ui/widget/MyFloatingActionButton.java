package com.zhanghao.gankio.ui.widget;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import com.zhanghao.gankio.R;
import skin.support.content.res.SkinCompatResources;
import skin.support.widget.SkinCompatHelper;
import skin.support.widget.SkinCompatImageHelper;
import skin.support.widget.SkinCompatSupportable;

import static skin.support.widget.SkinCompatHelper.INVALID_ID;

/**
 * Created by zhanghao on 2017/5/4.
 */

public class MyFloatingActionButton extends FloatingActionButton implements SkinCompatSupportable{

    private Context mContext;
    private ObjectAnimator mHideAnim;
    private ObjectAnimator mShowAnim;
    private boolean mIsHidden;
    private float mPxHeight;

    private int mRippleColorResId = 0;
    private int mBackgroundTintResId = 0;
    private SkinCompatImageHelper mImageHelper;

    public MyFloatingActionButton(Context context){
        this(context,null);
    }

    public MyFloatingActionButton(Context context, AttributeSet attrs) {
        this(context, attrs,0);

    }


    public MyFloatingActionButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FloatingActionButton,
                defStyleAttr,R.style.Widget_Design_FloatingActionButton);
        mBackgroundTintResId = a.getResourceId(R.styleable.FloatingActionButton_backgroundTint,INVALID_ID);
        mRippleColorResId = a.getResourceId(R.styleable.FloatingActionButton_rippleColor, INVALID_ID);
        a.recycle();
        applyBackgroundTintResource();
        applyBackgroundTintResource();
        mImageHelper = new SkinCompatImageHelper(this);
        mImageHelper.loadFromAttributes(attrs, defStyleAttr);
        mContext=context;
        init();
    }



    private void applyBackgroundTintResource() {
        mBackgroundTintResId = SkinCompatHelper.checkResourceId(mBackgroundTintResId);
        if (mBackgroundTintResId != INVALID_ID) {
            setBackgroundTintList(SkinCompatResources.getInstance().getColorStateList(mBackgroundTintResId));
        }
    }


    private void applyRippleColorResource() {
        mRippleColorResId = SkinCompatHelper.checkResourceId(mRippleColorResId);
        if (mRippleColorResId != INVALID_ID) {
            setRippleColor(SkinCompatResources.getInstance().getColor(mRippleColorResId));
        }
    }



    private void init() {
        mIsHidden = false;
        mPxHeight = dp2px(130);
        mHideAnim = ObjectAnimator.ofFloat(this, "translationY", 0, mPxHeight)
                .setDuration(200);
        mShowAnim = ObjectAnimator.ofFloat(this, "translationY", mPxHeight, 0)
                .setDuration(200);
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
        applyBackgroundTintResource();
        applyRippleColorResource();

    }
}
