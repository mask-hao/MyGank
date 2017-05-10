package com.zhanghao.gankio.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by zhanghao on 2017/5/1.
 */
public class GankFavs {

    private boolean error;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private String message;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public List<GankFavContent> getResult() {
        return result;
    }

    public void setResult(List<GankFavContent> result) {
        this.result = result;
    }

    private List<GankFavContent> result;
}
