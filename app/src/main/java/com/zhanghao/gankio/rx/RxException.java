package com.zhanghao.gankio.rx;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import retrofit2.HttpException;

/**
 * Created by zhanghao on 2017/4/22.
 */

public class RxException{
    private static final String TAG = "RxException";
    //网络请求异常码
    private  static final String TIME_OUT="连接超时";
    private  static final String CONNECTION_ERROR="网络连接异常";
    private  static final String CLIENT_ERROR="客户端请求异常";
    private  static final String SERVER_ERROR="服务器请求异常";
    private static final String UNKNOW_ERROR="未知错误";

    public  static String getErrorMessage(Throwable t){

//        t.printStackTrace();
//        LogUtil.d(TAG,t.getMessage());
//        LogUtil.d(TAG,t.getClass().getSimpleName());
        if (t instanceof SocketTimeoutException)
            return TIME_OUT;
        if (t instanceof ConnectException)
            return CONNECTION_ERROR;
        if (t instanceof HttpException){
            HttpException httpException= (HttpException) t;
            return progressHttpErrorCode(httpException.code());
        }
        return CONNECTION_ERROR;
    }

    private static String progressHttpErrorCode(int t) {
        int errorCode=t/100;
        switch (errorCode){
            case 4:
                return CLIENT_ERROR;
            case 5:
                return SERVER_ERROR;
            default:
                return UNKNOW_ERROR;
        }
    }


}
