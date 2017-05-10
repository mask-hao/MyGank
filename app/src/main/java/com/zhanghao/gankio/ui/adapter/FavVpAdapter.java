package com.zhanghao.gankio.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.zhanghao.gankio.ui.fragment.FavFragment;

import java.util.List;

/**
 * Created by zhanghao on 2017/5/4.
 */

public class FavVpAdapter extends FragmentPagerAdapter {

    private String[] titles;

    public FavVpAdapter(FragmentManager fm, String[] list) {
        super(fm);
        this.titles = list;
    }

    @Override
    public Fragment getItem(int position) {
        return FavFragment.newInstance(titles[position]);
    }

    @Override
    public int getCount() {
        return titles.length;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

    }


    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
