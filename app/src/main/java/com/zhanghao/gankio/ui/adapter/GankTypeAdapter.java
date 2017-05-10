package com.zhanghao.gankio.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.zhanghao.gankio.BaseApplication;
import com.zhanghao.gankio.R;
import com.zhanghao.gankio.entity.Constant;
import com.zhanghao.gankio.entity.GankContent;
import com.zhanghao.gankio.listener.LikeListener;
import com.zhanghao.gankio.util.ComUtil;
import com.zhanghao.gankio.util.LogUtil;
import com.zhanghao.gankio.util.NetWorkUtil;
import com.zhanghao.gankio.util.SharedPrefsUtils;

import java.util.List;

/**
 * Created by zhanghao on 2017/4/30.
 */

public class GankTypeAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {
    private SparseArray<Integer> heightArray;
    private int mWidth;
    private Context context;
    private LikeListener likeListener;
    private static final String TAG = "GankTypeAdapter";

    public GankTypeAdapter(List<MultiItemEntity> data, LikeListener likeListener) {
        super(data);
        this.context = BaseApplication.getContext();
        heightArray=new SparseArray<>();
        mWidth= (int)ComUtil.getScreenWidth(context)/2;
        addItemType(Constant.IMG, R.layout.gankimore_img_layout);
        addItemType(Constant.CONTENT, R.layout.gankitem_layout);
        addItemType(Constant.CONTENT_IMG, R.layout.gankitem_layout_with_img);
        this.likeListener = likeListener;
    }

    @Override
    protected void convert(BaseViewHolder helper, MultiItemEntity item) {

        LikeButton likeBt = null;
        if (helper.getItemViewType() == Constant.CONTENT_IMG || helper.getItemViewType() == Constant.CONTENT) {
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
            case Constant.CONTENT_IMG:
                ImageView iv = helper.getView(R.id.gankitem_iv);
                if (item instanceof GankContent) {
                    GankContent gankContent = (GankContent) item;
                    likeBt.setTag(gankContent.get_id());
                    helper.setText(R.id.gankitem_title_tv, gankContent.getDesc());
                    helper.setText(R.id.gankitem_source_tv, "来源：" + gankContent.getSource());
                    helper.setText(R.id.gankitem_who_tv, "作者：" + gankContent.getWho());
                    boolean fav = gankContent.isFav();

                    LogUtil.d(TAG, String.valueOf(fav));

                    if (!fav && likeBt.getTag().equals(gankContent.get_id()))
                        likeBt.setLiked(false);
                    else
                        likeBt.setLiked(true);
                    boolean wifi = NetWorkUtil.isWifiConnected(context);
                    boolean wifi_only = SharedPrefsUtils.getBooleanPreference(context, Constant.WIFI_ONLY, false);
                    if (wifi_only) {
                        if (wifi)
                            Glide.with(context).load(gankContent.getImages().get(0)).centerCrop().placeholder(R.drawable.noimage).into(iv);
                    } else {
                        Glide.with(context).load(gankContent.getImages().get(0)).placeholder(R.drawable.noimage).centerCrop().into(iv);
                    }
                }
                break;
            case Constant.CONTENT:
                if (item instanceof GankContent) {
                    GankContent gankContent = (GankContent) item;
                    likeBt.setTag(gankContent.get_id());
                    helper.setText(R.id.gankitem_title_tv, gankContent.getDesc());
                    helper.setText(R.id.gankitem_source_tv, "来源：" + gankContent.getSource());
                    helper.setText(R.id.gankitem_who_tv, "作者：" + gankContent.getWho());
                    boolean fav = gankContent.isFav();
                    LogUtil.d(TAG, String.valueOf(fav));
                    if (!fav && likeBt.getTag().equals(gankContent.get_id()))
                        likeBt.setLiked(false);
                    else
                        likeBt.setLiked(true);
                }
                break;
            case Constant.IMG:
                ImageView photoIv = helper.getView(R.id.ganktype_photo_iv);
                setPhotos(helper,item,photoIv);
//                if (item instanceof GankContent) {
//                    GankContent gankContent = (GankContent) item;
//                    Glide.with(context)
//                            .load(gankContent.getUrl())
//                            .into(photoIv);
//                }
                break;
        }
    }



    private void setPhotos(BaseViewHolder helper, MultiItemEntity item, ImageView photoIv){
        GankContent content= (GankContent) item;
        int position=helper.getLayoutPosition();
        if (heightArray.get(position)==null){
            Glide.with(context)
                    .load(content.getUrl())
                    .asBitmap()
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            int originalHeight=resource.getHeight();
                            int originalWidth=resource.getWidth();
                            FrameLayout.LayoutParams layoutParams=
                                    (FrameLayout.LayoutParams) photoIv.getLayoutParams();
                            int height=resizeHeight(originalHeight,originalWidth);
                            layoutParams.height=height;
                            photoIv.setLayoutParams(layoutParams);
                            heightArray.put(position,height);
                        }
                        private int resizeHeight(int originalHeight,int originalWidth) {
                            int scale=originalWidth/mWidth;
                            return originalHeight/scale;
                        }
                    });
        }else{
            int height=heightArray.get(position);
            FrameLayout.LayoutParams layoutParams=
                    (FrameLayout.LayoutParams) photoIv.getLayoutParams();
            layoutParams.height=height;
            photoIv.setLayoutParams(layoutParams);
        }
        Glide.with(context)
                .load(content.getUrl())
                .fitCenter()
                .into(photoIv);
    }


}
