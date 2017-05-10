package com.zhanghao.gankio.util;

import android.util.Log;

/**
 * Created by zhanghao on 2017/4/20.
 */

public class LogUtil {
    private static final int VERBOSE = 1;
    private static final int DEBUG=2;
    public static final int INFO=3;
    private static final int WARN=4;
    private static final int ERROR=5;
    private static final int NOTHING=6;

    private static int level=VERBOSE;


   public static void d(String tag,String msg){
       if (level<=DEBUG)
           Log.d(tag, msg);
   }

   public static void d(String tag,int mgs){
       if (level<=DEBUG){
           String str=String.valueOf(mgs);
           Log.d(tag,str);
       }
   }

    public static void i(String tag,String msg){
        if (level<=INFO)
            Log.i(tag, msg);
    }

    public static void w(String tag,String msg){
        if (level<=WARN)
            Log.w(tag, msg);
    }

    public static void e(String tag,String msg){
        if (level<=ERROR)
            Log.e(tag, msg);
    }

}
