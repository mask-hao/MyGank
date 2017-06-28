package com.zhanghao.gankio.ui.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhanghao.gankio.R;
import com.zhanghao.gankio.entity.GankContent;
import com.zhanghao.gankio.util.ComUtil;

import java.util.List;

/**
 * Created by zhanghao on 2017/6/24.
 */

public class RecommendPhotoAdapter extends BaseQuickAdapter<GankContent,BaseViewHolder>{


    private Context context;
    private int width;

    public RecommendPhotoAdapter(int layoutResId, List<GankContent> data,Context context) {
        super(layoutResId, data);
        this.context = context;
        width= (int) (ComUtil.getScreenWidth(context)/3);
    }

    @Override
    protected void convert(BaseViewHolder helper, GankContent item) {
        ImageView imageView = helper.getView(R.id.img_9_iv);
        Glide.with(context).load(item.getUrl()).override(width,width).into(imageView);
    }
}
