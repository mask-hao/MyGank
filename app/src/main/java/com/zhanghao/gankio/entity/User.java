package com.zhanghao.gankio.entity;

import android.content.Context;

import com.google.gson.Gson;
import com.zhanghao.gankio.util.SharedPrefsUtils;

/**
 * Created by 张浩 on 2017/1/20.
 */
public class User{

    private int userId;
    private String userName;
    private String userAccount;
    private String userPassword;
    private String userImage;

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }

    public String getVerifyId() {
        return verifyId;
    }

    public void setVerifyId(String verifyId) {
        this.verifyId = verifyId;
    }

    private String verifyCode;
    private String verifyId;

    public boolean isLogin() {
        return login;
    }

    public void setLogin(boolean login) {
        this.login = login;
    }

    private boolean login;

    private User() {
    }

    ;

    public static User getInstance() {
        return UserHolder.instance;
    }



    private static class UserHolder {
        private static final User instance = new User();
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    private String userToken;


    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
