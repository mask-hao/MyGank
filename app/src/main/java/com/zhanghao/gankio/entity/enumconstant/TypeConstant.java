package com.zhanghao.gankio.entity.enumconstant;

import com.zhanghao.gankio.entity.Constant;

/**
 * Created by zhanghao on 2017/6/13.
 */

public enum  TypeConstant {
    ANDROID("Android", Constant.BASE_RES_URL+"android.jpg"),
    IOS("iOS",Constant.BASE_RES_URL+"ios.jpg"),
    WEB("前端",Constant.BASE_RES_URL+"web.jpg"),
    PHOTO("福利",Constant.BASE_RES_URL+"photo.jpg"),
    VIDEO("休息视频",Constant.BASE_RES_URL+"video.jpg"),
    EXPANDRES("拓展资源",Constant.BASE_RES_URL+"expended.jpg"),
    RECOMMEND("瞎推荐",Constant.BASE_RES_URL+"recommend.jpg"),
    APP("App",Constant.BASE_RES_URL+"app.jpg");
    public String name;
    public String url;
    private TypeConstant(String name, String url) {
        this.name = name;
        this.url = url;
    }
}
