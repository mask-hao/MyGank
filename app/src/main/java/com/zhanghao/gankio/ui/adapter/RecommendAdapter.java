package com.zhanghao.gankio.ui.adapter;
import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.zhanghao.gankio.R;
import com.zhanghao.gankio.entity.Constant;
import com.zhanghao.gankio.entity.GankContent;
import com.zhanghao.gankio.entity.GankSection;
import com.zhanghao.gankio.entity.RecommendPhoto;
import com.zhanghao.gankio.ui.widget.DividerGridItemDecoration;
import com.zhanghao.gankio.ui.widget.ItemDecorationAlbumColumns;
import com.zhanghao.gankio.util.ActivityUtil;
import com.zhanghao.gankio.util.ComUtil;
import com.zhanghao.gankio.util.SharedPrefsUtils;
import com.zhanghao.gankio.util.http.NetWorkUtil;

import java.util.List;

/**
 * Created by zhanghao on 2017/6/24.
 */

public class RecommendAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity,BaseViewHolder>{

    private Activity activity;
    private Context context;
    private LinearLayout.LayoutParams layoutParams;
    private ItemDecorationAlbumColumns albumColumns;
    public RecommendAdapter(List<MultiItemEntity> data,Activity activity) {
        super(data);
        this.activity = activity;
        context = activity.getApplicationContext();
        addItemType(Constant.CONTENT, R.layout.gankitem_layout);
        addItemType(Constant.CONTENT_IMG,R.layout.gankitem_layout_with_img);
        addItemType(Constant.IMG_9,R.layout.gankitem_9_imgs);
        addItemType(Constant.SECTION,R.layout.gank_recommend_section);
        int height = (int) (ComUtil.getScreenWidth(context)/3);
        layoutParams =new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,height*3);
        albumColumns = new ItemDecorationAlbumColumns(6,3);
    }

    @Override
    protected void convert(BaseViewHolder helper, MultiItemEntity item) {

        switch (helper.getItemViewType()) {

            case Constant.CONTENT_IMG:
                ImageView iv = helper.getView(R.id.gankitem_iv);
                if (item instanceof GankContent) {
                    GankContent gankContent = (GankContent) item;
                    helper.setText(R.id.gankitem_title_tv, gankContent.getDesc());
                    helper.setText(R.id.gankitem_source_tv, "来源：" + gankContent.getType());
                    helper.setText(R.id.gankitem_who_tv, "作者：" + gankContent.getWho());
                    helper.getView(R.id.gankitem_like_bt).setVisibility(View.GONE);
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
                    helper.getView(R.id.gankitem_like_bt).setVisibility(View.GONE);
                    helper.setText(R.id.gankitem_title_tv, gankContent.getDesc());
                    helper.setText(R.id.gankitem_source_tv, "来源：" + gankContent.getType());
                    helper.setText(R.id.gankitem_who_tv, "作者：" + gankContent.getWho());
                    }
                break;
            case Constant.IMG_9:
                RecommendPhoto recommendPhoto = (RecommendPhoto) item;
                List<GankContent> contents = recommendPhoto.getNine_photos();
                RecyclerView recyclerView = helper.getView(R.id.gank_9_Imgs_Rl);
                recyclerView.setLayoutParams(layoutParams);
                recyclerView.setLayoutManager(new GridLayoutManager(context,3));
                RecommendPhotoAdapter adapter = new RecommendPhotoAdapter(R.layout.gank_9_img_item,
                        contents,context);
                if (recyclerView.getTag()==null){
                    recyclerView.addItemDecoration(albumColumns);
                    recyclerView.setTag("already init");
                }
                recyclerView.setAdapter(adapter);
                adapter.setOnItemClickListener((adapter1, view, position) -> {
                    ActivityUtil.gotoPhotoActivityFromRecommend(activity,view,contents,position);
                });
                break;
            case Constant.SECTION:
                GankSection section = (GankSection) item;
                TextView textView = helper.getView(R.id.recommend_section_tv);
                textView.setText(section.getSection());
                break;
        }
    }
}
