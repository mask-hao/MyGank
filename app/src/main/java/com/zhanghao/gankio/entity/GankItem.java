package com.zhanghao.gankio.entity;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.zhanghao.gankio.contract.GankContract;

import java.util.List;

/**
 * Created by zhanghao on 2017/4/20.
 */

public class GankItem{

    @SerializedName("error")
    private boolean error;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public List<GankContent> getResult() {
        return result;
    }

    public void setResult(List<GankContent> result) {
        this.result = result;
    }

    @SerializedName("results")
    private List<GankContent> result;

}
