package com.zhanghao.gankio.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.zhanghao.gankio.R;
import com.zhanghao.gankio.ui.adapter.PhotoAdapter;
import com.zhanghao.gankio.util.PermissionListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zhanghao on 2017/4/30.
 */

public class PhotoActivity extends BaseActivity{
    @BindView(R.id.photo_vp)
    ViewPager photoVp;
    @BindView(R.id.gank_photo_pos_tv)
    TextView gankPhotoPosTv;
    @BindView(R.id.photo_share)
    ImageView photoShare;
    @BindView(R.id.photo_save)
    ImageView photoSave;
    private ArrayList<String> mUrls;
    private ArrayList<String> mIds;
    private int curPos;
    private PhotoAdapter photoAdapter;
    private String URL;
    private String ID;
//    private Bitmap sharedBitmap = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideSystemUI();
        setContentView(R.layout.photo_layout);
        ButterKnife.bind(this);
        initIntentData();
        initView();
    }


    private void initIntentData() {
        Intent intent = getIntent();
        mUrls = intent.getStringArrayListExtra("urls");
        mIds = intent.getStringArrayListExtra("ids");
        curPos = intent.getIntExtra("position", 0);
        URL = mUrls.get(curPos);
        ID = mIds.get(curPos);
    }

    private void initView() {

        photoAdapter = new PhotoAdapter(this, mUrls);
        photoVp.setAdapter(photoAdapter);
        photoVp.setOffscreenPageLimit(1);
        photoVp.setCurrentItem(curPos);
        gankPhotoPosTv.setText(String.valueOf(curPos + 1) + "/" + String.valueOf(mUrls.size()));
        photoVp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                gankPhotoPosTv.setText(String.valueOf(position + 1) + "/" + String.valueOf(mUrls.size()));
                URL = mUrls.get(position);
                ID = mIds.get(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        photoShare.setOnClickListener(v->{
            BaseActivity.requestRunTimePermissions(BaseActivity.COMMON_PERMISSIONS, new PermissionListener() {
                @Override
                public void onGranted() {
                    photoShare();
                }
                @Override
                public void onDenied(List<String> deniedPermissions) {
                    String s = "缺少";
                    for (String deniedPermission : deniedPermissions) {
                        s+=deniedPermission;
                    }
                    s+="等权限，无法分享";
                    Toast.makeText(PhotoActivity.this,s,Toast.LENGTH_SHORT).show();
                }
            });

        });

        photoSave.setOnClickListener(v->{
            BaseActivity.requestRunTimePermissions(BaseActivity.COMMON_PERMISSIONS, new PermissionListener() {
                @Override
                public void onGranted() {
                    photoSave();
                }

                @Override
                public void onDenied(List<String> deniedPermissions) {
                    String s = "缺少";
                    for (String deniedPermission : deniedPermissions) {
                        s+=deniedPermission;
                    }
                    s+="等权限，无法保存";
                    Toast.makeText(PhotoActivity.this,s,Toast.LENGTH_SHORT).show();
                }
            });
        });
    }


    private void hideSystemUI() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    private void photoShare(){
     Glide.with(this).load(URL).asBitmap().into(new SimpleTarget<Bitmap>() {
         @Override
         public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
             if (resource==null||createUri(resource)==null){
                 Toast.makeText(PhotoActivity.this,"创建分享失败！",Toast.LENGTH_SHORT).show();
                 return;
             }
             Uri uri = createUri(resource);
             Intent intent = new Intent();
             intent.setAction(Intent.ACTION_SEND);
             intent.setType("image/jpeg");
             intent.putExtra(Intent.EXTRA_STREAM,uri);
             startActivity(Intent.createChooser(intent,"分享到..."));

         }
     });
    }

    private Uri createUri(Bitmap bitmap){
        File file =new File(Environment.getExternalStorageDirectory(),"MyGank");
        if (!file.exists())
            file.mkdirs();
        File image = new File(file.getPath(),"share.jpg");
        try{
            if (!image.exists()){
                image.createNewFile();
            }
            FileOutputStream out  = new  FileOutputStream(image);
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,out);
            String filePath = "file:"+image.getAbsolutePath();
            return Uri.parse(filePath);
        }catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }

    private void photoSave(){
        Glide.with(this).load(URL).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                photoSave(resource,ID);
            }
        });
    }


    private void photoSave(Bitmap  bitmap, String ids){
        File file =new File(Environment.getExternalStorageDirectory(),"MyGank");
        if (!file.exists())
            file.mkdirs();
        File image = new File(file.getPath(),ids+".jpg");
        try{
            if (!image.exists()){
                image.createNewFile();
            }else{
                Toast.makeText(this,"该图片已经保存",Toast.LENGTH_SHORT).show();
                return;
            }
            FileOutputStream out  = new  FileOutputStream(image);
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,out);
            Toast.makeText(this,"成功保存到："+image.getPath(),Toast.LENGTH_SHORT).show();
        }catch (IOException e){
            e.printStackTrace();
            Toast.makeText(this,"保存失败",Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus){
            hideSystemUI();
        }
    }
}
