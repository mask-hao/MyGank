package com.zhanghao.gankio.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.zhanghao.gankio.R;
import com.zhanghao.gankio.ui.adapter.PhotoAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zhanghao on 2017/4/30.
 */

public class PhotoActivity extends AppCompatActivity {
    @BindView(R.id.photo_vp)
    ViewPager photoVp;
    @BindView(R.id.gank_photo_pos_tv)
    TextView gankPhotoPosTv;
    private ArrayList<String> mDatas;
    private int curPos;
    private PhotoAdapter photoAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideSystemUI();
        setContentView(R.layout.photo_layout);
        ButterKnife.bind(this);
        initIntentData();
        initView();
    }


    private void initIntentData() {
        Intent intent = getIntent();
        mDatas = intent.getStringArrayListExtra("urls");
        curPos = intent.getIntExtra("position", 0);
    }

    private void initView() {
        photoAdapter = new PhotoAdapter(this, mDatas);
        photoVp.setAdapter(photoAdapter);
        photoVp.setCurrentItem(curPos);
        gankPhotoPosTv.setText(String.valueOf(curPos+1)+"/"+String.valueOf(mDatas.size()));
        photoVp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                gankPhotoPosTv.setText(String.valueOf(position+1)+"/"+String.valueOf(mDatas.size()));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }



    private void hideSystemUI() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
