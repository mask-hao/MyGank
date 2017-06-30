package com.zhanghao.gankio.ui.adapter;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhanghao.gankio.R;
import com.zhanghao.gankio.entity.Tag;
import com.zhanghao.gankio.entity.enumconstant.TypeConstant;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by zhanghao on 2017/6/24.
 */

public class TagAdapter extends BaseQuickAdapter<Tag, BaseViewHolder> {

    private Context context;
    private List<Tag> selectTags = new ArrayList<>();

    public TagAdapter(Context context, int layoutResId, List<Tag> data) {
        super(layoutResId, data);
        this.context = context;
        selectTags.add(data.get(0));
        selectTags.add(data.get(1));
        selectTags.add(data.get(2));
    }

    @Override
    protected void convert(BaseViewHolder helper, Tag item) {
        CircleImageView tagIv = helper.getView(R.id.tag_img_iv);
        TextView selectTv =helper.getView(R.id.tag_select_tv);
        CardView selectCv = helper.getView(R.id.tag_select_card);


        helper.setText(R.id.tag_text_tv, item.getType());
        String url = getUrl(item.getType());
        Glide.with(context).load(url).into(tagIv);
        selectTv.setText("+");


        if (selectTags.contains(item)){
            selectCv.setTag(true);
            selectTv.setText("-");
            selectCv.setCardBackgroundColor(ContextCompat.getColor(context,R.color.colorTextViewGrey));
        }else{
            selectCv.setTag(false);
            selectCv.setCardBackgroundColor(ContextCompat.getColor(context,R.color.colorPrimary));
        }

        helper.getConvertView().setOnClickListener(v -> {
            if (selectTags.contains(item)){
                selectTags.remove(item);
                selectCv.setTag(false);
                selectCv.setCardBackgroundColor(ContextCompat.getColor(context,R.color.colorPrimary));
                selectTv.setText("+");
                showAnimation(selectCv);
            }else{
                selectTags.add(item);
                selectCv.setTag(true);
                selectCv.setCardBackgroundColor(ContextCompat.getColor(context,R.color.colorTextViewGrey));
                selectTv.setText("-");
               showAnimation(selectCv);
            }
        });


    }


    private void showAnimation(CardView cardView){
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(cardView,"scaleX",1,0.7f);
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(cardView,"scaleY",1,0.7f);
        ObjectAnimator animator3 = ObjectAnimator.ofFloat(cardView,"scaleX",0.7f,1);
        ObjectAnimator animator4 = ObjectAnimator.ofFloat(cardView,"scaleY",0.7f,1);
        AnimatorSet animatorSet =new AnimatorSet();
        animatorSet.playTogether(animator1,animator2,animator3,animator4);
        animatorSet.setDuration(100);
        animatorSet.start();
    }



    private String getUrl(String type) {
        switch (type) {
            case "Android":
                return TypeConstant.ANDROID.url;
            case "iOS":
                return TypeConstant.IOS.url;
            case "休息视频":
                return TypeConstant.VIDEO.url;
            case "瞎推荐":
                return TypeConstant.RECOMMEND.url;
            case "App":
                return TypeConstant.APP.url;
            case "前端":
                return TypeConstant.WEB.url;
            case "拓展资源":
                return TypeConstant.EXPANDRES.url;
            case "福利":
                return TypeConstant.PHOTO.url;
        }
        return null;
    }


    public List<Tag> getSelectTags() {
        return selectTags;
    }
}
