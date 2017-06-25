package com.zhanghao.gankio.entity;

/**
 * Created by zhanghao on 2017/6/22.
 */
public class Tag {
    private int id;
    private String type;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    @Override
    public String toString() {
        return "Tag{" +
                "id=" + id +
                ", type='" + type + '\'' +
                '}';
    }
}
