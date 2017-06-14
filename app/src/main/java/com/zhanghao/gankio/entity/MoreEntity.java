package com.zhanghao.gankio.entity;

import com.zhanghao.gankio.entity.enumconstant.TypeConstant;

/**
 * Created by zhanghao on 2017/4/29.
 */

public class MoreEntity{
    private String type;
    private String resId;


    public MoreEntity(String type, String resId) {
        this.type = type;
        this.resId = resId;
    }

    public MoreEntity(TypeConstant constant){
        this.type = constant.name;
        this.resId = constant.url;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getResId() {
        return resId;
    }

    public void setResId(String resId) {
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
