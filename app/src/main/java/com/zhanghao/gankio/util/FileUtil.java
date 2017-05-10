package com.zhanghao.gankio.util;

import android.content.Context;

import java.io.File;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by zhanghao on 2017/5/5.
 */

public class FileUtil {
    public static String calculateCacheSize(Context context){
        File file=context.getCacheDir();
        return getFormatSize(getCacheSize(file));
    }


    public static boolean clearCache(Context context){
        File file=context.getCacheDir();
        return deleteDir(file);
    }


    private static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }


    private static long getCacheSize(File f){
        long size = 0;
        File [] files=f.listFiles();
        for (File file : files) {
            if (file.isDirectory())
                size+=getCacheSize(file);
            else
                size+=file.length();
        }
        return size;
    }



    private static String getFormatSize(double size) {
        double kiloByte = size / 1024;
        if (kiloByte < 1) {
            return "0K";
        }

        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "K";
        }

        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "M";
        }

        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()
                + "TB";
    }


}
