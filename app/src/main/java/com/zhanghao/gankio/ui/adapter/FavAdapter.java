package com.zhanghao.gankio.ui.adapter;

import android.content.Context;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.zhanghao.gankio.BaseApplication;
import com.zhanghao.gankio.R;
import com.zhanghao.gankio.entity.Constant;
import com.zhanghao.gankio.entity.GankFavContent;
import com.zhanghao.gankio.util.http.NetWorkUtil;
import com.zhanghao.gankio.util.SharedPrefsUtils;

import java.util.List;

/**
 * Created by zhanghao on 2017/5/4.
 */

public class FavAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder>{

    private Context context;



    public FavAdapter(List<MultiItemEntity> data) {
        super(data);
        this.context = BaseApplication.getContext();
        addItemType(Constant.CONTENT, R.layout.gankfav_layout);
        addItemType(Constant.CONTENT_IMG, R.layout.gankfav_layout_with_img);
    }


    @Override
    protected void convert(BaseViewHolder helper, MultiItemEntity item) {

        switch (helper.getItemViewType()) {
            case Constant.CONTENT_IMG:
                ImageView iv = helper.getView(R.id.gankitem_iv);
                if (item instanceof GankFavContent) {
                    GankFavContent content = (GankFavContent) item;
                    helper.setText(R.id.gankitem_title_tv, content.getDesc());
                    helper.setText(R.id.gankitem_source_tv, "来源：" + content.getSource());
                    helper.setText(R.id.gankitem_who_tv, "作者：" + content.getWho());

                    boolean wifi = NetWorkUtil.isWifiConnected(context);
                    boolean wifi_only = SharedPrefsUtils.getBooleanPreference(context, Constant.WIFI_ONLY, false);
                    if (wifi_only) {
                        if (wifi)
                            Glide.with(context).load(content.getImages()).placeholder(R.drawable.noimage).centerCrop().into(iv);
                    } else {
                        Glide.with(context).load(content.getImages()).placeholder(R.drawable.noimage).centerCrop().into(iv);
                    }
                }
                break;

            case Constant.CONTENT:
                if (item instanceof GankFavContent) {
                    GankFavContent content = (GankFavContent) item;
                    helper.setText(R.id.gankitem_title_tv, content.getDesc());
                    helper.setText(R.id.gankitem_source_tv, "来源：" + content.getSource());
                    helper.setText(R.id.gankitem_who_tv, "作者：" + content.getWho());
                }
                break;
        }


    }




}
