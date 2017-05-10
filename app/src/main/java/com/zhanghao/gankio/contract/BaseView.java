package com.zhanghao.gankio.contract;

public interface BaseView<T>{
    void showDialog();
    void hideDialog();
    void showError(String message);
    void setPresenter(T presenter);
}
