package com.zhanghao.gankio.ui.widget;

import com.chad.library.adapter.base.loadmore.LoadMoreView;
import com.zhanghao.gankio.R;


/**
 * Created by zhanghao on 2017/4/9.
 */

public class CustomLoadMore extends LoadMoreView{

    @Override
    public int getLayoutId() {
        return R.layout.loadmore_layout;
    }

    @Override
    protected int getLoadingViewId() {
        return R.id.load_more_loading_view;
    }

    @Override
    protected int getLoadFailViewId() {
        return R.id.load_more_load_fail_view;
    }

    @Override
    protected int getLoadEndViewId() {
        return R.id.load_more_load_end_view;
    }
}
