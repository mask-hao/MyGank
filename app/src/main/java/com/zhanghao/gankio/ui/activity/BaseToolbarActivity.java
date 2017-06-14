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

public abstract class BaseToolbarActivity<P extends BasePresenter> extends BaseActivity{

    protected P mPresenter;
    private static final String TAG = "BaseActivity";
    abstract protected int setContentLayout();
    abstract protected boolean canBack();
    protected MyToolbar mToolbar;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(setContentLayout());
        ButterKnife.bind(this);
        mToolbar=(MyToolbar) findViewById(R.id.toolbar);
        if (mToolbar==null) throw new IllegalStateException("The BaseToolbarActivity must be contain a toolbar");
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mPresenter!=null)
            mPresenter.unSubscribe();

    }




}
