package com.zhanghao.gankio.entity;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

/**
 * Created by zhanghao on 2017/6/24.
 */

public class RecommendPhoto implements MultiItemEntity {

    private List<GankContent> nine_photos;

    public List<GankContent> getNine_photos() {
        return nine_photos;
    }

    public void setNine_photos(List<GankContent> nine_photos) {
        this.nine_photos = nine_photos;
    }

    @Override
    public int getItemType() {
        return Constant.IMG_9;
    }


    @Override
    public String toString() {
        return "RecommendPhoto{" +
                "nine_photos=" + nine_photos +
                '}';
    }
}
