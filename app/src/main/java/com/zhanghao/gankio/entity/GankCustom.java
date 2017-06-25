package com.zhanghao.gankio.entity;

import java.util.List;

/**
 * Created by zhanghao on 2017/6/22.
 */
public class GankCustom {
    private boolean error;
    private List<GankContent> itemList;
    private List<GankContent> photos;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public List<GankContent> getItemList() {
        return itemList;
    }

    public void setItemList(List<GankContent> itemList) {
        this.itemList = itemList;
    }


    public List<GankContent> getPhotos() {
        return photos;
    }

    public void setPhotos(List<GankContent> photos) {
        this.photos = photos;
    }

    @Override
    public String toString() {
        return "GankCustom{" +
                "error=" + error +
                ", itemList=" + itemList +
                ", photos=" + photos +
                '}';
    }
}
