package com.zhanghao.gankio.listener;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

/**
 * Created by zhanghao on 2017/4/10.
 */

public abstract class RecyclerScrollListener extends RecyclerView.OnScrollListener{
    private static final int SCROLL_DISTANCE = 35;
    private int totalScrollDistance;

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        int firstVPos=0;
        RecyclerView.LayoutManager layoutManager=recyclerView.getLayoutManager();
        if (layoutManager instanceof LinearLayoutManager){
            LinearLayoutManager linearLayoutManager= (LinearLayoutManager)layoutManager;
            firstVPos=linearLayoutManager.findFirstVisibleItemPosition();
        }
        if (layoutManager instanceof StaggeredGridLayoutManager){
            StaggeredGridLayoutManager staggeredGridLayoutManager= (StaggeredGridLayoutManager) layoutManager;
            firstVPos=staggeredGridLayoutManager.findFirstVisibleItemPositions(new int[2])[0];
        }

        firstVisibleItemPosition(firstVPos);
        if (firstVPos == 0){
          return;
        }else {
            if (totalScrollDistance>SCROLL_DISTANCE){
                hideToolBar();
                hideBottomBar();
                totalScrollDistance=0;
            }else if(totalScrollDistance<-SCROLL_DISTANCE ){
                showToolBar();
                showBottomBar();
                totalScrollDistance=0;
            }
        }
        if (dy>0||dy<0){
            totalScrollDistance+=dy;
        }
//        if (dy>0) hideBottomBar();
//        if (dy<0) showBottomBar();

    }

    public abstract void hideToolBar();

    public abstract void showToolBar();

    public abstract void hideBottomBar();

    public abstract void showBottomBar();

    public abstract void firstVisibleItemPosition(int position);


}
