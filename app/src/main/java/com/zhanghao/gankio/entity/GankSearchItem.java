package com.zhanghao.gankio.entity;

/**
 * Created by zhanghao on 2017/5/5.
 */
public class GankSearchItem {
    private String url;
    private String type;
    private String who;
    private String title;

    public GankSearchItem(String url, String type, String who, String title) {
        this.url = url;
        this.type = type;
        this.who = who;
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getWho() {
        return who;
    }

    public void setWho(String who) {
        this.who = who;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "GankSearchItem{" +
                "url='" + url + '\'' +
                ", type='" + type + '\'' +
                ", who='" + who + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
