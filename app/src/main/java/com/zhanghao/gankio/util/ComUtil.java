package com.zhanghao.gankio.util;

import android.content.Context;
import android.graphics.Point;
import android.view.WindowManager;

import com.zhanghao.gankio.entity.Constant;

import java.security.MessageDigest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zhanghao on 2017/4/22.
 */

public class ComUtil {

    private static char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

    public static boolean isValidEmail(String emailString) {
        String mailRegEx = "\\w{3,20}@\\w+\\.(com|org|cn|net|gov)";
        Pattern mailPattern = Pattern.compile(mailRegEx);
        Matcher mailMatcher = mailPattern.matcher(emailString);
        return mailMatcher.matches();
    }


    public final static String getMD5(String s) {

        try {
            byte[] btInput = s.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (byte byte0 : md) {
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static int dp2px(Context context,float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static List<String> getRightCategories(List<String> list){
        List<String> category1=initCategory();
        List<String> category2=initCategory();
        category1.removeAll(list);
        category2.removeAll(category1);
        return category2;
    }

    private static List<String> initCategory() {
        List<String> list=new ArrayList<>();
        list.add(Constant.ANDROID);
        list.add(Constant.IOS);
        list.add(Constant.WEB);
        list.add(Constant.EXPANDRES);
        list.add(Constant.RECOMMEND);
        list.add(Constant.APP);
        list.add(Constant.VIDEO);
        return list;
    }

    public static String getFormatDate(String date){
        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
        Date date1;
        try {
            date1 = dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
        dateFormat.applyPattern("yyyy/MM/dd");
        return dateFormat.format(date1);
    }


    public static float getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int width;
        Point size = new Point();
        wm.getDefaultDisplay().getSize(size);
        width = size.x;
        return width;
    }


}
