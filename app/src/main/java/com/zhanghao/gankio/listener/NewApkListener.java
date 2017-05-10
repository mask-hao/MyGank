package com.zhanghao.gankio.listener;

/**
 * Created by zhanghao on 2017/5/10.
 */

public interface NewApkListener {
    void findNewApk(String updateInfo,String downloadUrl,long size);
}
