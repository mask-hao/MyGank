package com.zhanghao.gankio.util;

import android.content.Context;

import com.zhanghao.gankio.BaseApplication;
import com.zhanghao.gankio.entity.Constant;
import com.zhanghao.gankio.entity.User;

import java.io.Serializable;

/**
 * Created by zhanghao on 2017/4/22.
 */

public class UserUtil{

    public static void serializeUser(User user){
        Context context=BaseApplication.getContext();
        if (user.getUserAccount()!=null&&!user.getUserAccount().isEmpty())
            SharedPrefsUtils.setStringPreference(context, Constant.USER_ACCOUNT,user.getUserAccount());

        if (user.getUserToken()!=null&&!user.getUserToken().isEmpty())
            SharedPrefsUtils.setStringPreference(context, Constant.USER_TOKEN,user.getUserToken());

        if (!user.getUserName().isEmpty()&&user.getUserName()!=null)
            SharedPrefsUtils.setStringPreference(context, Constant.USER_NAME,user.getUserName());

        if (!user.getUserImage().isEmpty()&&user.getUserImage()!=null)
            SharedPrefsUtils.setStringPreference(context, Constant.USER_IMAGE,user.getUserImage());
    }


    public static void clearUserInfo(Context context){
            SharedPrefsUtils.setStringPreference(context, Constant.USER_ACCOUNT,"");
            SharedPrefsUtils.setStringPreference(context, Constant.USER_TOKEN,"");
            SharedPrefsUtils.setStringPreference(context, Constant.USER_NAME,"");
            SharedPrefsUtils.setStringPreference(context, Constant.USER_IMAGE,"");
    }


    public static void initSerializedUser() {
        Context context = BaseApplication.getContext();
        User user = User.getInstance();
        String userName;
        String userToken;
        String userAccount;
        String userImage;

        userName = SharedPrefsUtils.getStringPreference(context, Constant.USER_NAME);
        userToken = SharedPrefsUtils.getStringPreference(context, Constant.USER_TOKEN);
        userAccount = SharedPrefsUtils.getStringPreference(context, Constant.USER_ACCOUNT);
        userImage=SharedPrefsUtils.getStringPreference(context,Constant.USER_IMAGE);

        user.setUserAccount(userAccount);
        user.setUserToken(userToken);
        user.setUserName(userName);
        user.setUserImage(userImage);
    }


    public static void saveVerifyId(Context context,String id){
        SharedPrefsUtils.setStringPreference(context, Constant.VERIFY_ID,id);
    }

    public static String getVerifyId(Context context){
        return SharedPrefsUtils.getStringPreference(context,Constant.VERIFY_ID);
    }


}
