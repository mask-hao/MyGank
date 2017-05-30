package com.zhanghao.gankio.presenter;
import com.google.gson.Gson;
import com.zhanghao.gankio.api.UserApi;
import com.zhanghao.gankio.api.UserService;
import com.zhanghao.gankio.contract.UserContract;
import com.zhanghao.gankio.entity.CommonResponse;
import com.zhanghao.gankio.entity.Constant;
import com.zhanghao.gankio.entity.User;
import com.zhanghao.gankio.rx.RxHelper;
import com.zhanghao.gankio.rx.RxObserver;
import com.zhanghao.gankio.util.LogUtil;

import okhttp3.MultipartBody;

/**
 * Created by zhanghao on 2017/4/22.
 */

public class UserPresenter extends BasePresenterImpl implements UserContract.Presenter{

    private static final String TAG = "UserPresenter";
    private UserContract.View mView;
    private UserContract.LogoutView mLogoutView;
    private UserService mUserService;

    public UserPresenter(UserContract.View mView) {
        this.mView = mView;
        mView.setPresenter(this);
        mUserService = UserApi.getInstance().userService;
    }


    public UserPresenter(UserContract.LogoutView mView) {
        this.mLogoutView = mView;
        mLogoutView.setPresenter(this);
        mUserService = UserApi.getInstance().userService;
    }

    @Override
    public void login(User user) {
        LogUtil.d(TAG,"登录："+user.toString());
        mView.showDialog();
        mUserService.login(user)
                .compose(RxHelper.toUI())
                .subscribe(new RxObserver<CommonResponse<User>>(this) {
                    @Override
                    protected void onSuccess(CommonResponse<User> userCommonResponse) {
                        LogUtil.d(TAG,new Gson().toJson(userCommonResponse));
                        switch (userCommonResponse.getResult()){
                            case Constant.LOGIN_SUCCESS:
                                User userInfo=userCommonResponse.getContent();

                                LogUtil.d(TAG+"USerinfo: ",userInfo.toString());

                                mView.loginResult(userInfo);
                                break;
                            case Constant.LOGIN_ERROR:
                                onFail(userCommonResponse.getResult());
                                break;
                            case Constant.USER_INVALID:
                                onFail(userCommonResponse.getResult());
                                break;
                            default:
                                onError(new UnknownError());
                                break;
                        }
                        mView.hideDialog();
                    }
                    @Override
                    protected void onFail(String message) {
                        mView.showError(message);
                    }});
    }

    @Override
    public void register(User user) {
        LogUtil.d(TAG,"注册："+user.toString());
        mView.showDialog();
        mUserService.register(user).compose(RxHelper.toUI())
                .subscribe(new RxObserver<CommonResponse<String>>(this) {
                    @Override
                    protected void onSuccess(CommonResponse<String> stringCommonResponse) {

                       LogUtil.d(TAG,new Gson().toJson(stringCommonResponse));

                       switch (stringCommonResponse.getResult()){
                           case Constant.REGISTER_SUCCESS:
//                               mView.registerResult(stringCommonResponse.getResult());
                               login(user);
                               break;
                           case Constant.REGISTER_ERROR:
                               onFail(stringCommonResponse.getResult());
                               break;
                           default:
                               onError(new UnknownError());
                               break;
                       }
                       mView.hideDialog();
                    }
                    @Override
                    protected void onFail(String message) {
                        mView.showError(message);
                    }
                });
    }

    @Override
    public void logout(User user) {
        mUserService.logout(user).compose(RxHelper.toUI())
                .subscribe(new RxObserver<CommonResponse<String>>(this) {
                    @Override
                    protected void onSuccess(CommonResponse<String> stringCommonResponse) {
                        if (stringCommonResponse.getResult().equals(Constant.LOGOUT_SUCCESS))
                            mLogoutView.logoutResult(stringCommonResponse.getContent());
                        else
                            onFail(stringCommonResponse.getResult());
                    }

                    @Override
                    protected void onFail(String message) {
                        mLogoutView.logoutResult(message);
                    }
                });
    }
    @Override
    public void uploadImg(MultipartBody.Part file, MultipartBody.Part token) {

        mUserService.uploadImg(file,token).compose(RxHelper.toUI())
                .subscribe(new RxObserver<CommonResponse<String>>(this) {
                    @Override
                    protected void onSuccess(CommonResponse<String> stringCommonResponse) {
                        switch (stringCommonResponse.getResult()){
                            case Constant.FILEUPLOAD_SUCCESS:
                                mView.uploadImgResult(stringCommonResponse.getContent());
                                break;
                            case Constant.FILEUPLOAD_ERROR:
                                onFail(stringCommonResponse.getContent());
                                break;
                            default:
                                onError(new UnknownError());
                                break;
                        }
                    }
                    @Override
                    protected void onFail(String message) {
                        mView.showError(message);
                    }
                });
    }

    @Override
    public void getVerifyCode(User user) {
        mUserService.getVerifyCode(user).compose(RxHelper.toUI())
                .subscribe(new RxObserver<CommonResponse<String>>(this) {
                    @Override
                    protected void onSuccess(CommonResponse<String> stringCommonResponse) {
                         switch (stringCommonResponse.getResult()){
                             case Constant.SEND_EMAIL_SUCCESS:
                                 mView.getVerifyCodeResult(stringCommonResponse.getContent());
                                 break;
                             case Constant.SEND_EMAIL_ERROR:
                                 onFail(stringCommonResponse.getContent());
                                 break;
                             default:
                                 onError(new UnknownError());
                                 break;
                         }
                    }
                    @Override
                    protected void onFail(String message) {
                        mView.showError(message);
                    }
                });
    }
}
