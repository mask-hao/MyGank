package com.zhanghao.gankio.contract;
import com.zhanghao.gankio.entity.User;
import com.zhanghao.gankio.presenter.BasePresenter;
import java.io.File;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by zhanghao on 2017/4/22.
 */

public interface UserContract{
    interface View extends BaseView<Presenter>{
        void loginResult(User user);
        void uploadImgResult(String path);
        void getVerifyCodeResult(String id);
    }

    interface LogoutView extends BaseView<Presenter>{
        void logoutResult(String message);
    }

     interface Presenter extends BasePresenter{
        void login(User user);
        void register(User user);
        void logout(User user);
        void uploadImg(MultipartBody.Part file, MultipartBody.Part token);
        void getVerifyCode(User user);
    }
}
