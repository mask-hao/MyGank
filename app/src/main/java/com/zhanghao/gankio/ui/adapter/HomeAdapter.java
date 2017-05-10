package com.zhanghao.gankio.ui.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.zhanghao.gankio.BaseApplication;
import com.zhanghao.gankio.R;
import com.zhanghao.gankio.entity.Constant;
import com.zhanghao.gankio.entity.GankContent;
import com.zhanghao.gankio.entity.GankSection;
import com.zhanghao.gankio.listener.LikeListener;
import com.zhanghao.gankio.util.ComUtil;
import com.zhanghao.gankio.util.LogUtil;
import com.zhanghao.gankio.util.NetWorkUtil;
import com.zhanghao.gankio.util.SharedPrefsUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by zhanghao on 2017/4/28.
 */

public class HomeAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {

    private static final String TAG = "HomeDataAdapter";
    private Context context;
    private LikeListener likeListener;

    public HomeAdapter(List<MultiItemEntity> data, LikeListener likeListener) {
        super(data);
        this.context = BaseApplication.getContext();
        addItemType(Constant.IMG, R.layout.gankitem_img_layout);
        addItemType(Constant.SECTION, R.layout.gankitem_section_layout);
        addItemType(Constant.CONTENT, R.layout.gankitem_layout);
        addItemType(Constant.CONTENT_IMG, R.layout.gankitem_layout_with_img);
        this.likeListener = likeListener;
    }

    @Override
    protected void convert(BaseViewHolder helper, MultiItemEntity item) {
        LikeButton likeBt = null;

        if (helper.getItemViewType() == Constant.CONTENT || helper.getItemViewType() == Constant.CONTENT_IMG) {
            likeBt = helper.getView(R.id.gankitem_like_bt);
            likeBt.setOnLikeListener(new OnLikeListener() {
                @Override
                public void liked(LikeButton likeButton) {
                    likeListener.onLiked(helper.getLayoutPosition() - 1);
                }

                @Override
                public void unLiked(LikeButton likeButton) {
                    likeListener.onUnLiked(helper.getLayoutPosition() - 1);
                }
            });
        }


        switch (helper.getItemViewType()) {
            case Constant.SECTION:
                if (item instanceof GankSection) {
                    GankSection section = (GankSection) item;
                    helper.setText(R.id.gankitem_section_tv, section.getSection());
                }
                break;
            case Constant.CONTENT_IMG:
                ImageView iv = helper.getView(R.id.gankitem_iv);

                if (item instanceof GankContent) {
                    GankContent gankContent = (GankContent) item;
                    helper.setText(R.id.gankitem_title_tv, gankContent.getDesc());
                    helper.setText(R.id.gankitem_source_tv, "来源：" + gankContent.getSource());
                    helper.setText(R.id.gankitem_who_tv, "作者：" + gankContent.getWho());
                    boolean fav = gankContent.isFav();
                    if (!fav)
                        likeBt.setLiked(false);
                    else
                        likeBt.setLiked(true);
                    boolean wifi = NetWorkUtil.isWifiConnected(context);
                    boolean wifi_only = SharedPrefsUtils.getBooleanPreference(context, Constant.WIFI_ONLY, false);
                    if (wifi_only) {
                        if (wifi)
                            Glide.with(context).load(gankContent.getImages().get(0)).placeholder(R.drawable.noimage).centerCrop().into(iv);
                    } else {
                        Glide.with(context).load(gankContent.getImages().get(0)).placeholder(R.drawable.noimage).centerCrop().into(iv);
                    }
                }

                break;
            case Constant.CONTENT:
                if (item instanceof GankContent) {
                    GankContent gankContent = (GankContent) item;
                    helper.setText(R.id.gankitem_title_tv, gankContent.getDesc());
                    helper.setText(R.id.gankitem_source_tv, "来源：" + gankContent.getSource());
                    helper.setText(R.id.gankitem_who_tv, "作者：" + gankContent.getWho());
                    boolean fav = gankContent.isFav();
                    if (!fav)
                        likeBt.setLiked(false);
                    else
                        likeBt.setLiked(true);
                }

                break;
            case Constant.IMG:
                ImageView ivHeader = helper.getView(R.id.gankitem_photo_iv);
                if (item instanceof GankSection) {
                    GankSection content = (GankSection) item;

                    Glide.with(context)
                            .load(content.getContent().getUrl())
                            .centerCrop()
                            .into(ivHeader);
                    String time = ComUtil.getFormatDate(content.getContent().getPublishedAt());
                    helper.setText(R.id.gankitem_time_tv, time);
                }
                break;
        }


    }


}
