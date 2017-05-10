package com.zhanghao.gankio.entity;

/**
 * Created by 张浩 on 2017/1/29.
 */
public class CommonResponse<T>{
    private String resultCode;

    public String getResult() {
        return resultCode;
    }

    public void setResult(String result) {
        this.resultCode = result;
    }

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }

    private T content;
}
