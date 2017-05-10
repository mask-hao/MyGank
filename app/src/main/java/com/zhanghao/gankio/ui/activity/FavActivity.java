package com.zhanghao.gankio.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.zhanghao.gankio.R;
import com.zhanghao.gankio.entity.Constant;
import com.zhanghao.gankio.ui.adapter.FavVpAdapter;

import butterknife.BindView;

/**
 * Created by zhanghao on 2017/5/3.
 */

public class FavActivity extends BaseActivity {
    @BindView(R.id.fav_vp)
    ViewPager favVp;
    @BindView(R.id.fav_tabL)
    TabLayout favTabL;
    private FavVpAdapter favAdapter;
    private String[] titles = {
            Constant.ANDROID,
            Constant.IOS,
            Constant.WEB,
            Constant.APP,
            Constant.RECOMMEND,
            Constant.EXPANDRES,
            Constant.VIDEO};

    @Override
    protected int setContentLayout() {
        return R.layout.fav_layout;
    }

    @Override
    protected boolean canBack() {
        return true;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.favorite_tv);
        initView();
    }

    private void initView() {
        favAdapter=new FavVpAdapter(getSupportFragmentManager(),titles);
        favVp.setAdapter(favAdapter);
        favTabL.setupWithViewPager(favVp);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
