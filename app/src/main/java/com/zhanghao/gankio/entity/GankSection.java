package com.zhanghao.gankio.entity;
import com.chad.library.adapter.base.entity.MultiItemEntity;
/**
 * Created by zhanghao on 2017/4/28.
 */

public class GankSection implements MultiItemEntity{

    private int itemType;

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    private String section;

    public GankContent getContent() {
        return content;
    }

    public void setContent(GankContent content) {
        this.content = content;
    }

    private GankContent content;

    public GankSection(int itemType, GankContent content) {
        this.itemType = itemType;
        this.content = content;
    }

    public GankSection(int itemType, String section) {
        this.itemType = itemType;
        this.section = section;
    }

    @Override
    public int getItemType() {
        return itemType;
    }
}
