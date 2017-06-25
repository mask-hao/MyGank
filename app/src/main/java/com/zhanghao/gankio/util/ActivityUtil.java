package com.zhanghao.gankio.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.zhanghao.gankio.entity.GankContent;
import com.zhanghao.gankio.entity.GankSection;
import com.zhanghao.gankio.ui.activity.DetailActivity;
import com.zhanghao.gankio.ui.activity.DialogActivity;
import com.zhanghao.gankio.ui.activity.FavActivity;
import com.zhanghao.gankio.ui.activity.GankTypeListActivity;
import com.zhanghao.gankio.ui.activity.MainActivity;
import com.zhanghao.gankio.ui.activity.PhotoActivity;
import com.zhanghao.gankio.ui.activity.SearchActivity;
import com.zhanghao.gankio.ui.activity.SettingActivity;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by zhanghao on 2017/3/17.
 */

public class ActivityUtil {
    private static final String TAG = "ActivityUtil";


    public static void gotoMainActivity(Context context){
        context.startActivity(new Intent(context, MainActivity.class));
    }

    public static void gotoSettingActivity(Context context){
        Intent intent=new Intent(context, SettingActivity.class);
        context.startActivity(intent);
    }
    public static void gotoDetailActivity(Context context,String url,String title){
        Intent intent=new Intent(context, DetailActivity.class);
        intent.putExtra("url",url);
        intent.putExtra("title",title);
        context.startActivity(intent);
    }

    public static void gotoTypeListActivity(Context context,String type){
        Intent intent=new Intent(context, GankTypeListActivity.class);
        intent.putExtra("type",type);
        context.startActivity(intent);
    }

    public static void gotoPhotoActivity(Context context,List<MultiItemEntity> list,int pos){
        ArrayList<String> urls=new ArrayList<>();
        ArrayList<String> ids = new ArrayList<>();
        for (MultiItemEntity itemEntity : list) {
            if (itemEntity instanceof GankContent){
                GankContent content = (GankContent) itemEntity;
                urls.add(content.getUrl());
                ids.add(content.get_id());
            }
        }
        Intent intent = new Intent(context, PhotoActivity.class);
        intent.putStringArrayListExtra("urls",urls);
        intent.putStringArrayListExtra("ids",ids);
        intent.putExtra("position",pos);
        context.startActivity(intent);
    }



    public static void gotoPhotoActivityFromRecommend(Context context,List<GankContent> list,int pos){
        ArrayList<String> urls=new ArrayList<>();
        ArrayList<String> ids = new ArrayList<>();
        for (GankContent content : list) {
                urls.add(content.getUrl());
                ids.add(content.get_id());
        }
        Intent intent = new Intent(context, PhotoActivity.class);
        intent.putStringArrayListExtra("urls",urls);
        intent.putStringArrayListExtra("ids",ids);
        intent.putExtra("position",pos);
        context.startActivity(intent);
    }




    public static void gotoPhotoActivityH(Context context,List<GankSection> list,
                                          List<MultiItemEntity> itemEntities,int pos){
        ArrayList<String> urls=new ArrayList<>();
        ArrayList<String> ids=new ArrayList<>();
        GankSection section= (GankSection) itemEntities.get(pos);
        int realPos=list.indexOf(section);
        LogUtil.d(TAG,realPos);

        for (GankSection gankSection : list) {
            urls.add(gankSection.getContent().getUrl());
            ids.add(gankSection.getContent().get_id());
        }

        Intent intent = new Intent(context, PhotoActivity.class);
        intent.putStringArrayListExtra("urls",urls);
        intent.putStringArrayListExtra("ids",ids);
        intent.putExtra("position",realPos);
        context.startActivity(intent);
    }

    public static void gotoFavActivity(Context context) {
        context.startActivity(new Intent(context,FavActivity.class));
    }

    public static void gotoSearchActivity(Context context) {
        context.startActivity(new Intent(context, SearchActivity.class));
    }

    public static void gotoSetting(Context context) {
        Uri uri=Uri.parse("package:" + context.getPackageName());
        context.startActivity(new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,uri));
    }

    public static void gotoDialogActivity(Context context,String changeLog, String url, long size) {
        Intent intent = new Intent(context, DialogActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("changeLog",changeLog);
        intent.putExtra("url",url);
        intent.putExtra("size",size);
        context.startActivity(intent);
    }

    public static void gotoAboutBrowser(Context context) {
        Uri uri = Uri.parse("https://github.com/mask-hao/MyGank");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        context.startActivity(intent);
    }
}
