package com.zhanghao.gankio.ui.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.zhanghao.gankio.R;
import com.zhanghao.gankio.contract.UserContract;
import com.zhanghao.gankio.entity.Constant;
import com.zhanghao.gankio.entity.User;
import com.zhanghao.gankio.presenter.UserPresenter;
import com.zhanghao.gankio.ui.activity.BaseActivity;
import com.zhanghao.gankio.util.ActivityUtil;
import com.zhanghao.gankio.util.ComUtil;
import com.zhanghao.gankio.util.ImageUtil;
import com.zhanghao.gankio.util.LogUtil;
import com.zhanghao.gankio.util.PermissionListener;
import com.zhanghao.gankio.util.SharedPrefsUtils;
import com.zhanghao.gankio.util.UserUtil;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import skin.support.SkinCompatManager;

import static android.app.Activity.RESULT_OK;

/**
 * Created by zhanghao on 2017/3/17.
 */

public class MeFragment extends BaseFragment<UserContract.Presenter> implements UserContract.View {
    private static final String TAG = "MeFragment";
    private static final int SELECT_IMAGE = 200;
    @BindView(R.id.login_bt)
    Button loginBt;
    @BindView(R.id.me_iv)
    CircleImageView meIv;
    @BindView(R.id.me_tv)
    TextView meTv;
    Unbinder unbinder;
    @BindView(R.id.login_rl)
    RelativeLayout loginRl;
    @BindView(R.id.me_rl)
    RelativeLayout meRl;
    @BindView(R.id.me_fav_ll)
    LinearLayout meFavLl;
    @BindView(R.id.me_nightTh_ll)
    LinearLayout meNightThLl;
    @BindView(R.id.me_setting_ll)
    LinearLayout meSettingLl;
    @BindView(R.id.me_nightTh_sw)
    Switch meNightThSw;
//    private UserContract.Presenter mUserPresenter;
    private AlertDialog alertDialog;
    boolean loginState = true;
    LinearLayout loginLLView;
    ContentLoadingProgressBar progressBar;
    LinearLayout registerLLView;


    private boolean night = false;

    public static MeFragment getInstance() {
        MeFragment meFragment = new MeFragment();
        new UserPresenter(meFragment);
        return meFragment;
    }


    @Override
    public int getLayoutId() {
        return R.layout.fragment_me;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = getRoot(inflater, container);
        unbinder = ButterKnife.bind(this, root);
        initView();
        initUserInfo();
        return root;
    }

    private void initView() {
        meNightThSw.setChecked(SharedPrefsUtils.getBooleanPreference(getContext(),"night",false));
        meIv.setOnClickListener(v -> uploadUserImg());
        meSettingLl.setOnClickListener(v -> ActivityUtil.gotoSettingActivity(getContext()));
        meFavLl.setOnClickListener(v -> {
            ActivityUtil.gotoFavActivity(getContext());
        });


        meNightThLl.setOnClickListener(v -> {
            showThemeChangeAnimation();
            if (!SharedPrefsUtils.getBooleanPreference(getContext(),"night",false)){
                SkinCompatManager.getInstance().loadSkin("night.skin", new SkinCompatManager.SkinLoaderListener() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onFailed(String errMsg) {
                        LogUtil.d(TAG,errMsg);
                    }
                });
                SharedPrefsUtils.setBooleanPreference(getContext(),"night",true);
                meNightThSw.setChecked(true);
            }else{
                SkinCompatManager.getInstance().restoreDefaultTheme();
                SharedPrefsUtils.setBooleanPreference(getContext(),"night",false);
                meNightThSw.setChecked(false);
            }
        });
    }

    private void uploadUserImg() {
        initPermission();
    }

    private void initPermission() {
        BaseActivity.requestRunTimePermissions(BaseActivity.COMMON_PERMISSIONS, new PermissionListener() {
            @Override
            public void onGranted() {
                selectImage();
            }

            @Override
            public void onDenied(List<String> deniedPermissions) {
                Toast.makeText(getContext(), "缺少 " + deniedPermissions.get(0) + "等权限", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, SELECT_IMAGE);
    }

    private void initUserInfo() {
        User user = User.getInstance();
//        LogUtil.d(TAG + "initUser", user.toString());
        if (!user.getUserToken().isEmpty()) {
            meRl.setVisibility(View.VISIBLE);
            meRl.setAlpha(1);
            loginRl.setVisibility(View.GONE);
            meTv.setText(user.getUserName());
            if (!user.getUserImage().isEmpty()) {
                Glide.with(this)
                        .load(Constant.BASE_URL + "/" + user.getUserImage())
                        .into(meIv);
            }
            //自动登录
            // TODO: 2017/4/23 bug
            mPresenter.login(user);
        }
        loginBt.setOnClickListener(v -> showLoginDialog());
    }

    private void showLoginDialog() {
        final View viewDialog = LayoutInflater.from(getContext()).inflate(R.layout.dialog_login, null);
        progressBar = (ContentLoadingProgressBar) viewDialog.findViewById(R.id.progress_cpb);

        TextInputEditText loginAccountEt = (TextInputEditText) viewDialog.findViewById(R.id.login_account_et);
        TextInputEditText loginPasswordEt = (TextInputEditText) viewDialog.findViewById(R.id.login_password_et);

        TextInputEditText registerNameEt = (TextInputEditText) viewDialog.findViewById(R.id.register_name_et);
        TextInputEditText registerAccountEt = (TextInputEditText) viewDialog.findViewById(R.id.register_account_et);
        TextInputEditText registerPasswordEt1 = (TextInputEditText) viewDialog.findViewById(R.id.register_password_et1);
        TextInputEditText registerPasswordEt2 = (TextInputEditText) viewDialog.findViewById(R.id.register_password_et2);
        TextInputEditText registerVerifyCodeEt = (TextInputEditText) viewDialog.findViewById(R.id.register_verifyCode_et);


        Button login_bt = (Button) viewDialog.findViewById(R.id.login_bt);
        Button register_bt = (Button) viewDialog.findViewById(R.id.register_bt);
        Button getVerifyCode_bt = (Button) viewDialog.findViewById(R.id.get_verifyCode_bt);


        loginLLView = (LinearLayout) viewDialog.findViewById(R.id.login_ll);

        registerLLView = (LinearLayout) viewDialog.findViewById(R.id.register_ll);

        alertDialog = new AlertDialog.Builder(getContext())
                .setTitle(R.string.login_register)
                .setView(viewDialog)
                .setCancelable(true)
                .create();
        alertDialog.show();
        alertDialog.setOnDismissListener(dialog -> loginState = true);
        login_bt.setOnClickListener(v -> {
            getLoginInfo(loginAccountEt, loginPasswordEt);
        });

        register_bt.setOnClickListener(v -> {
            if (loginState) {
                dismissLoginLLView();
                showRegisterLLView();
                login_bt.setVisibility(View.GONE);
                loginState = false;
            } else {
                getRegisterInfo(registerNameEt, registerAccountEt, registerPasswordEt1, registerPasswordEt2, registerVerifyCodeEt);
            }
        });

        getVerifyCode_bt.setOnClickListener(v -> {
            String account = registerAccountEt.getText().toString().trim();
            if (account.isEmpty()) {
                registerAccountEt.setError("邮箱不可为空");
                return;
            }
            if (!ComUtil.isValidEmail(account)) {
                registerAccountEt.setError("邮箱格式不正确");
                return;
            }
            User user = User.getInstance();
            user.setUserAccount(account);
            mPresenter.getVerifyCode(user);
        });


    }

    private void getRegisterInfo(TextInputEditText registerNameEt,
                                 TextInputEditText registerAccountEt,
                                 TextInputEditText registerPasswordEt1,
                                 TextInputEditText registerPasswordEt2,
                                 TextInputEditText registerVerifyCodeEt) {
        String userName = registerNameEt.getText().toString().trim();
        String userAccount = registerAccountEt.getText().toString().trim();
        String password1 = registerPasswordEt1.getText().toString().trim();
        String password2 = registerPasswordEt2.getText().toString().trim();
        String verifyCode = registerVerifyCodeEt.getText().toString().trim();

        if (userName.isEmpty()) {
            registerNameEt.setError("昵称不能为空");
            return;
        }
        if (userAccount.isEmpty()) {
            registerAccountEt.setError("账号不能为空");
            return;
        }
        if (password1.isEmpty()) {
            registerPasswordEt1.setError("此密码不能为空");
            return;
        }
        if (password2.isEmpty()) {
            registerPasswordEt2.setError("此密码不能为空");
            return;
        }
        if (password1.length() < 6) {
            registerPasswordEt1.setError("密码位数不得少于6位");
            return;
        }
        if (!password1.equals(password2)) {
            registerPasswordEt1.setError("密码不一致");
            return;
        }
        if (verifyCode.isEmpty()) {
            registerVerifyCodeEt.setError("验证码不能为空");
            return;
        }
        User user = User.getInstance();
        user.setUserAccount(userAccount);
        user.setUserPassword(ComUtil.getMD5(password1));
        user.setUserName(userName);
        user.setUserToken("");
        user.setVerifyCode(verifyCode);
        user.setVerifyId(UserUtil.getVerifyId(getContext()));
        mPresenter.register(user);
    }

    private void getLoginInfo(TextInputEditText loginAccountEt,
                              TextInputEditText loginPasswordEt) {
        String email = loginAccountEt.getText().toString().trim();
        String password = loginPasswordEt.getText().toString().trim();
        if (email.isEmpty()) {
            loginAccountEt.setError("邮箱不能为空");
            return;
        } else if (!ComUtil.isValidEmail(email)) {
            loginAccountEt.setError("邮箱格式错误");
            return;
        }
        if (password.isEmpty()) {
            loginPasswordEt.setError("密码不能为空");
            return;
        }
        //// TODO: 2017/4/22 md5算法加密密码
        User user = User.getInstance();
        user.setUserAccount(email);
        user.setUserPassword(ComUtil.getMD5(password));
        user.setUserToken("");
        mPresenter.login(user);
    }

    private void showRegisterLLView() {
        registerLLView.setVisibility(View.VISIBLE);
        loginLLView.setVisibility(View.GONE);
        ObjectAnimator.ofFloat(registerLLView, "alpha", 0, 1)
                .setDuration(500)
                .start();

    }

    private void dismissLoginLLView() {
        ObjectAnimator.ofFloat(loginLLView, "alpha", 1, 0)
                .setDuration(500)
                .start();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void showDialog() {
        if (loginLLView != null && loginLLView.getVisibility() == View.VISIBLE && loginState)
            loginLLView.setAlpha(0.3f);
        if (registerLLView != null && registerLLView.getVisibility() == View.VISIBLE && !loginState)
            registerLLView.setAlpha(0.3f);
        if (progressBar != null)
            progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideDialog() {
        if (alertDialog != null)
            alertDialog.dismiss();
    }

    @Override
    public void showError(String message) {
        if (loginLLView != null && loginLLView.getAlpha() == 0.3f) {
            loginLLView.setAlpha(1);
            if (progressBar != null)
                progressBar.setVisibility(View.GONE);
        }
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        if (message.equals(Constant.USER_INVALID)) {
            resetLoginView();
            UserUtil.clearUserInfo(getContext());
        }
        if (meRl.getVisibility() == View.VISIBLE)
            meRl.setAlpha(1);
    }

    @Override
    public void setPresenter(UserContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void loginResult(User user) {
        setupUser(user);
        LogUtil.d(TAG + "LoginResult:", user.toString());
        ObjectAnimator.ofFloat(loginRl, "alpha", 1, 0).setDuration(500).start();
        setUserInfo();
        ObjectAnimator.ofFloat(meRl, "alpha", 0, 1).setDuration(500).start();
        UserUtil.serializeUser(user);
        if (!loginState)
            loginState = true;
        if (!user.getUserImage().equals(""))
            Glide.with(getContext()).load(Constant.BASE_URL + "/" + user.getUserImage()).into(meIv);
    }

    private void setupUser(User user1) {
        User user = User.getInstance();
        user.setUserAccount(user1.getUserAccount());
        user.setUserToken(user1.getUserToken());
        user.setUserName(user1.getUserName());
        user.setUserImage(user1.getUserImage());
        user.setLogin(true);
        user.setUserId(user1.getUserId());
    }

    @Override
    public void uploadImgResult(String path) {
        Glide.with(getContext())
                .load(Constant.BASE_URL + "/" + path)
                .into(meIv);
        User.getInstance().setUserImage(path);
        UserUtil.serializeUser(User.getInstance());
    }

    @Override
    public void getVerifyCodeResult(String id) {
        //保存verify id 有效期五分钟
        UserUtil.saveVerifyId(getContext(), id);
    }

    private void resetLoginView() {
        meRl.setVisibility(View.GONE);
        loginRl.setVisibility(View.VISIBLE);
        loginRl.setAlpha(1);
    }


    private void setUserInfo() {
        loginRl.setVisibility(View.GONE);
        meRl.setVisibility(View.VISIBLE);
        meTv.setText(User.getInstance().getUserName());
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_IMAGE && resultCode == RESULT_OK) {
            File file = ImageUtil.processSelectedIntent(getContext(), data);
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpg"), file);
            String userToken = User.getInstance().getUserToken();
            if (userToken == null) return;
            LogUtil.d(TAG + "  token ", userToken);
            MultipartBody.Part partFile = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
            MultipartBody.Part partToken = MultipartBody.Part.createFormData("token", userToken);
            mPresenter.uploadImg(partFile, partToken);
        }
    }



    /**
     * 切换的动画
     */
    private void showThemeChangeAnimation() {
        final View decorView=getActivity().getWindow().getDecorView();
        Bitmap cacheBitmap=getCacheBitmapFromView(decorView);
        if (decorView instanceof ViewGroup && cacheBitmap!=null){
            final View view=new View(getContext());
            view.setBackgroundDrawable(new BitmapDrawable(getResources(),cacheBitmap));
            ViewGroup.LayoutParams layoutParams=new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            ((ViewGroup)decorView).addView(view,layoutParams);
            ObjectAnimator objectAnimator=ObjectAnimator.ofFloat(view,"alpha", 1f, 0f);
            objectAnimator.setDuration(500);
            objectAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    ((ViewGroup)decorView).removeView(view);
                }
            });
            objectAnimator.start();
        }
    }

    /**
     * 获取一个View的缓存视图
     * @param view
     * @return
     */
    private Bitmap getCacheBitmapFromView(View view){
        final boolean drawingCacheEnable=true;
        view.setDrawingCacheEnabled(drawingCacheEnable);
        view.buildDrawingCache(drawingCacheEnable);
        final Bitmap drawingCache=view.getDrawingCache();
        Bitmap bitmap;
        if (drawingCache!=null){
            bitmap=Bitmap.createBitmap(drawingCache);
            view.setDrawingCacheEnabled(false);
        }else {
            bitmap=null;
        }
        return bitmap;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
