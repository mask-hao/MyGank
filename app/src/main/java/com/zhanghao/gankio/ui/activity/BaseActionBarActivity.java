package com.zhanghao.gankio.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import com.zhanghao.gankio.R;
import com.zhanghao.gankio.ui.widget.MyToolbar;
import com.zhanghao.gankio.util.ActivityPool;

import butterknife.ButterKnife;
import skin.support.app.SkinCompatActivity;

/**
 * Created by zhanghao on 2017/5/18.
 */

public abstract class  BaseActionBarActivity extends SkinCompatActivity{


    abstract protected int setContentLayout();
    abstract protected boolean canBack();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityPool.addActivity(this);
        setContentView(setContentLayout());
        ButterKnife.bind(this);
        setUpToolBar();
    }


    protected void setUpToolBar() {
        if (canBack()) {
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


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityPool.removeActivity(this);
    }


}
