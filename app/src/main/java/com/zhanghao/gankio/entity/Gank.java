package com.zhanghao.gankio.entity;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by zhanghao on 2017/4/20.
 */

public class Gank {

    @Override
    public String toString() {
        return "Gank{" +
                "error=" + error +
                ", result=" + result +
                ", category=" + category +
                '}';
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public ResultsBean getResult() {
        return result;
    }

    public void setResult(ResultsBean result) {
        this.result = result;
    }

    public List<String> getCategory() {
        return category;
    }

    public void setCategory(List<String> category) {
        this.category = category;
    }

    @SerializedName("error")
    private boolean error;

    @SerializedName("results")
    private ResultsBean result;

    @SerializedName("category")
    private List<String> category;

    public static class ResultsBean{

        public List<GankContent> getAndroid() {
            return android;
        }

        public void setAndroid(List<GankContent> android) {
            this.android = android;
        }

        public List<GankContent> getiOS() {
            return iOS;
        }

        public void setiOS(List<GankContent> iOS) {
            this.iOS = iOS;
        }

        public List<GankContent> getVideo() {
            return video;
        }

        public void setVideo(List<GankContent> video) {
            this.video = video;
        }

        public List<GankContent> getWeb() {
            return web;
        }

        public void setWeb(List<GankContent> web) {
            this.web = web;
        }

        public List<GankContent> getPhoto() {
            return photo;
        }

        public void setPhoto(List<GankContent> photo) {
            this.photo = photo;
        }

        public List<GankContent> getRecommend() {
            return recommend;
        }

        public void setRecommend(List<GankContent> recommend) {
            this.recommend = recommend;
        }

        public List<GankContent> getExtend() {
            return extend;
        }

        public void setExtend(List<GankContent> extend) {
            this.extend = extend;
        }

        public List<GankContent> getApp() {
            return app;
        }

        public void setApp(List<GankContent> app) {
            this.app = app;
        }

        @SerializedName("Android")
        private List<GankContent> android;

        @SerializedName("iOS")
        private List<GankContent> iOS;

        @SerializedName("休息视频")
        private List<GankContent> video;

        @SerializedName("前端")
        private List<GankContent> web;

        @SerializedName("福利")
        private List<GankContent> photo;

        @SerializedName("瞎推荐")
        private List<GankContent> recommend;

        @SerializedName("拓展资源")
        private List<GankContent> extend;

        @SerializedName("App")
        private List<GankContent> app;


        @Override
        public String toString() {
            return "ResultsBean{" +
                    "android=" + android +
                    ", iOS=" + iOS +
                    ", video=" + video +
                    ", web=" + web +
                    ", photo=" + photo +
                    ", recommend=" + recommend +
                    ", extend=" + extend +
                    ", app=" + app +
                    '}';
        }
    }
}
