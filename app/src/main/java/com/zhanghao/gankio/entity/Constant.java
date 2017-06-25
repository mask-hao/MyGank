package com.zhanghao.gankio.entity;


/**
 * Created by 张浩 on 2017/1/23.
 */
public class Constant {
    //json数据状态码result
    public static final String LOGIN_SUCCESS="登录成功";
    public static final String REGISTER_SUCCESS="注册成功";
    public static final String LOGIN_ERROR="登录失败";
    public static final String REGISTER_ERROR="注册失败";
    public static final String USER_INVALID="用户无效，请重新登录";
    public static final String ACCOUNT_EMPTY="账号不可为空";
    public static final String PASSWORD_EMPTY="密码不可为空";
    public static final String USERNAME_EMPTY="用户名不可为空";
    public static final String FILEUPLOAD_SUCCESS="文件上传成功";
    public static final String FILEUPLOAD_ERROR="文件上传失败";
    public static final String FILE_EMPTY="上传文件不可为空";
    public static final String IO_ERROR="io异常";
    public static final String TOKEN_EMPTY = "用户凭证不可为空";
    public static final String DBUPDATE_ERROR = "数据库更新异常";
    public static final String UPDATEUSERINFO_SUCCESS = "更新用户信息成功";
    public static final String UPDATEUSERINFO_ERROR= "更新用户信息失败";
    public static final String LOGOUT_SUCCESS = "登出成功";
    public static final String LOGOUT_ERROR= "登出失败";
    public static final String ADDFAV_SUCCESS = "收藏成功";
    public static final String ADDFAV_ERROR = "收藏失败";
    public static final String DELETE_FAV_SUCCESS="删除收藏成功";
    public static final String DELETE_FAV_ERROR="删除收藏失败";
    public static final String ACCOUNT_USED = "该账号已经被注册";
    public static final String UPDATEPASS_SUCCESS = "密码更新成功";
    public static final String UPDATEPASS_ERROR = "密码更新失败";
    public static final String OLDPASS_ERROR = "旧密码错误";
    public static final String NO_MORE_DATA="暂无更多数据";

    public static final String GET_DATE_SUCCESS="获取日期成功";
    public static final String GET_DATE_FAILED="获取日期失败";

    public static final String SEND_EMAIL_SUCCESS="发送邮件成功";
    public static final String SEND_EMAIL_ERROR="发送邮件失败";
    public static final String VERIFY_CODE_ERROR="验证码错误";
    public static final String VERIFY_CODE_INVALIDATE="验证码失效";


    public static final String GET_DATA_SUCCESS="获取数据成功";
    public static final String  GET_DATA_FAILED="获取数据失败";


    public static final String GET_SEARCH_DATA_SUCCESS="获取搜索数据正确";
    public static final String GET_SEARCH_DATA_ERROR="获取搜索数据错误";




    public static final String GET_TAGS_SUCCESS="获取标签成功";
    public static final String GET_TAGS_FAILED="获取标签失败";

    public static final String UPDATE_TAGS_SUCCESS="上传标签成功";
    public static final String UPDATE_TAGS_FAILED="上传标签失败";


    public static final String SAVE_IN_LOCALDB = "保存在本地数据库";


    public static final String ANDROID="Android";
    public static final String IOS="iOS";
    public static final String WEB="前端";
    public static final String PHOTO="福利";
    public static final String VIDEO="休息视频";
    public static final String EXPANDRES="拓展资源";
    public static final String RECOMMEND="瞎推荐";
    public static final String APP="App";

    //常量数据码
    public static final String BASE_IMG_URL = "http://115.159.23.58:8080/";
    public static final String BASE_URL = "http://115.159.23.58:8080/GankWeb/api/";
    public static final String BASE_RES_URL = "http://115.159.23.58:8080/resource/images/";
    public static final String USER_TOKEN = "token";
    public static final String USER_NAME = "username";
    public static final String USER_ACCOUNT="account";
    public static final String USER_IMAGE="image";
    public static final String VERIFY_ID="verify_id";




    public static final int SECTION=100;
    public static final int CONTENT=101;
    public static final int CONTENT_IMG=102;
    public static final int IMG=103;
    public static final int IMG_9=104;


    public static final String WIFI_ONLY="wifi_only";



    public static final int GET_APP_INFO=900;
    public static final int DOWNLOAD_NEW_APK=901;
    public static final String FIR_CODE="fir_code";
    public static final String FIR_DOWNLOAD_URL="fir_down_url";
    public static final String FIR_NEW_APK_SIZE="fir_new_apk_size";
}
