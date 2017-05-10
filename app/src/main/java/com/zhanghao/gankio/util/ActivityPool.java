package com.zhanghao.gankio.util;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhanghao on 2016/12/30.
 */

public class ActivityPool {
    private static List<Activity> activityList=new ArrayList<>();
    public static void addActivity(Activity activity){
        activityList.add(activity);
    }
    public static void removeActivity(Activity activity){
        activityList.remove(activity);
    }

    public static Activity getTopActivity(){
        if (!activityList.isEmpty())
            return activityList.get(activityList.size()-1);
        else
            return null;
    }

    public static void finishAll(){
        for (Activity activity : activityList) {
            if (!activity.isFinishing())
                activity.finish();
        }
    }
}
