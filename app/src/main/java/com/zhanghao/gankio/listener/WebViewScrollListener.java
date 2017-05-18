package com.zhanghao.gankio.listener;

import com.zhanghao.gankio.ui.widget.MyScrollWebView;

/**
 * Created by zhanghao on 2017/5/17.
 */
public abstract class WebViewScrollListener implements MyScrollWebView.MyScrollListener{

    private boolean isHide=false;


    @Override
    public void onScroll(int dy) {
        if (dy>20 && !isHide){
            hideToolbar();
            isHide=true;
        }else if (dy<-10 && isHide){
            showToolbar();
            isHide=false;
        }

    }


    public abstract void hideToolbar();
    public abstract void showToolbar();
}
