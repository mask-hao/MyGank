package com.zhanghao.gankio.util;
import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import com.zhanghao.gankio.R;
import com.zhanghao.gankio.entity.enumconstant.BottomConstant;
import com.zhanghao.gankio.ui.fragment.HomeFragment;
import com.zhanghao.gankio.ui.fragment.MeFragment;
import com.zhanghao.gankio.ui.fragment.MoreFragment;
/**
 * Created by zhanghao on 2017/3/17.
 */

public class FragmentUtil {
    private HomeFragment homeFragment;
    private MoreFragment moreFragment;
    private MeFragment meFragment;
    private FragmentManager mManager;
    private ActionBar actionBar;
    private FragmentTransaction mTransaction;
    private Context context;

    public FragmentUtil(AppCompatActivity activity){
        this.context=activity;
        mManager=activity.getSupportFragmentManager();
        actionBar=activity.getSupportActionBar();
    }

    public void initFragment(BottomConstant constant){
        mTransaction=mManager.beginTransaction();

        hideFragments(mTransaction);
        switch (constant){
            case HOME:
                actionBar.setTitle(HomeFragment.currentTitle);
                if (homeFragment==null){
                    homeFragment=HomeFragment.getInstance();
                    mTransaction.add(R.id.fragment_content,homeFragment);
                }else
                    mTransaction.show(homeFragment);
                break;
            case MORE:
                actionBar.setTitle(constant.name);
                if (moreFragment==null){
                    moreFragment=MoreFragment.getInstance();
                    mTransaction.add(R.id.fragment_content,moreFragment);
                }else
                    mTransaction.show(moreFragment);
                break;
            case ME:
                actionBar.setTitle(constant.name);
                if (meFragment==null){
                    meFragment=MeFragment.getInstance();
                    mTransaction.add(R.id.fragment_content,meFragment);
                }else
                    mTransaction.show(meFragment);
                break;

            default:
                return;
        }
        mTransaction.commit();
    }

    private void hideFragments(FragmentTransaction mTransaction) {
        if (homeFragment!=null) mTransaction.hide(homeFragment);
        if (moreFragment!=null) mTransaction.hide(moreFragment);
        if (meFragment!=null) mTransaction.hide(meFragment);

    }

}
