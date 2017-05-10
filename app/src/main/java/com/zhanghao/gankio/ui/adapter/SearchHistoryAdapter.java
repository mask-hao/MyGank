package com.zhanghao.gankio.ui.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhanghao.gankio.R;

import java.util.List;

/**
 * Created by zhanghao on 2017/5/5.
 */

public class SearchHistoryAdapter extends BaseQuickAdapter<String,BaseViewHolder>{

    public SearchHistoryAdapter(int layoutResId, List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
         helper.addOnClickListener(R.id.search_his_iv);
         helper.setText(R.id.search_his_tv,item);
    }
}
