package com.zhanghao.gankio.entity;

/**
 * Created by zhanghao on 2017/4/29.
 */

public class MoreEntity{
    private String type;
    private int resId;

    public MoreEntity(String type, int resId) {
        this.type = type;
        this.resId = resId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }


    @Override
    public String toString() {
        return "MoreEntity{" +
                "type='" + type + '\'' +
                ", resId=" + resId +
                '}';
    }
}
