package com.zhanghao.gankio.ui.adapter;

import android.content.Context;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhanghao.gankio.R;
import com.zhanghao.gankio.entity.MoreEntity;
import com.zhanghao.gankio.util.ComUtil;

import java.util.List;

/**
 * Created by zhanghao on 2017/4/29.
 */

public class MoreAdapter extends BaseItemDraggableAdapter<MoreEntity,BaseViewHolder> {
    private Context context;
    public MoreAdapter(int layoutResId, List<MoreEntity> data,Context context) {
        super(layoutResId, data);
        this.context=context;
    }

    @Override
    protected void convert(BaseViewHolder helper, MoreEntity item) {
        helper.setText(R.id.gankitemmore_type_tv, item.getType());
        ImageView iv = helper.getView(R.id.gankitemmore_photo_iv);
        float width = ComUtil.getScreenWidth(context);
        int halfW = (int) (width / 2);
        Glide.with(context).load(item.getResId()).override(halfW, halfW).into(iv);
    }
}
