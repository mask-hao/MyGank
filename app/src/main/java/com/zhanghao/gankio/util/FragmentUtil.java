package com.zhanghao.gankio.util;
import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import com.zhanghao.gankio.R;
import com.zhanghao.gankio.entity.enumconstant.BottomConstant;
import com.zhanghao.gankio.ui.fragment.DayFragment;
import com.zhanghao.gankio.ui.fragment.MeFragment;
import com.zhanghao.gankio.ui.fragment.MoreFragment;
import com.zhanghao.gankio.ui.fragment.RecommendFragment;

/**
 * Created by zhanghao on 2017/3/17.
 */

public class FragmentUtil {
    private DayFragment dayFragment;
    private RecommendFragment recommendFragment;
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
            case RECOMMEND:
                actionBar.setTitle(constant.name);
                if (recommendFragment==null){
                    recommendFragment=RecommendFragment.getInstance();
                    mTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    mTransaction.add(R.id.fragment_content,recommendFragment);
                }else{
                    if (!recommendFragment.isVisible())
                        mTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    mTransaction.show(recommendFragment);
                }

                break;
            case DAY:
                actionBar.setTitle(DayFragment.currentTitle);
                if (dayFragment ==null){
                    dayFragment = DayFragment.getInstance();
                    mTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    mTransaction.add(R.id.fragment_content, dayFragment);
                }else{
                    if (!dayFragment.isVisible())
                        mTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    mTransaction.show(dayFragment);
                }

                break;
            case MORE:
                actionBar.setTitle(constant.name);
                if (moreFragment==null){
                    moreFragment=MoreFragment.getInstance();
                    mTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    mTransaction.add(R.id.fragment_content,moreFragment);
                }else{
                    if (!moreFragment.isVisible())
                        mTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    mTransaction.show(moreFragment);
                }
                break;
            case ME:
                actionBar.setTitle(constant.name);
                if (meFragment==null){
                    meFragment=MeFragment.getInstance();
                    mTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    mTransaction.add(R.id.fragment_content,meFragment);
                }else{
                    if (!meFragment.isVisible())
                        mTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    mTransaction.show(meFragment);
                }
                break;
            default:
                return;
        }
        mTransaction.commit();
    }

    private void hideFragments(FragmentTransaction mTransaction) {
        if (dayFragment !=null) mTransaction.hide(dayFragment);
        if (moreFragment!=null) mTransaction.hide(moreFragment);
        if (meFragment!=null) mTransaction.hide(meFragment);
        if (recommendFragment!=null) mTransaction.hide(recommendFragment);
    }

}
