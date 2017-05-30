package com.zhanghao.gankio.ui.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhanghao.gankio.R;
import com.zhanghao.gankio.entity.GankSearchItem;

import java.util.List;

/**
 * Created by zhanghao on 2017/5/5.
 */

public class SearchResultAdapter extends BaseQuickAdapter<GankSearchItem,BaseViewHolder>{
    private static final String TAG = "SearchResultAdapter";

    public SearchResultAdapter(int layoutResId, List<GankSearchItem> data) {
        super(layoutResId, data);
    }
    @Override
    protected void convert(BaseViewHolder helper, GankSearchItem item) {
        String who=item.getWho();
        String type=item.getType();
        String title=item.getTitle();
        helper.setText(R.id.gank_search_title_tv,title);
        helper.setText(R.id.gank_search_who_tv,who);
        helper.setText(R.id.gank_search_type_tv,type);
    }
}
