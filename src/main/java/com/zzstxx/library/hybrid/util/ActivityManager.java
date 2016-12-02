package com.zzstxx.library.hybrid.util;

import android.app.Activity;

import java.util.ArrayList;

/**
 * 作者：孙博
 * <br>
 * 创建时间：Hiboboo on 2016/8/2.
 * <br>
 * 描述：Activity管理
 * <br>
 * 版本：
 */
public class ActivityManager
{
    private final static ArrayList<Activity> activitys = new ArrayList<>();

    public static void addActivity(Activity activity)
    {
        if (!activitys.contains(activity))
            activitys.add(activity);
    }

    public static void removeActivity(Activity activity)
    {
        if (activitys.contains(activity))
            activitys.remove(activity);
    }

    public static void finishActivitys()
    {
        for (Activity activity : activitys)
            if (!activity.isFinishing())
                activity.finish();
    }

    public static void finishSingleActivity(Activity activity)
    {
        if (null == activity)
            return;
        if (activitys.contains(activity))
            activitys.remove(activity);
        activity.finish();
    }

    public static void finishSingleActivityByClass(Class<? extends Activity> cls)
    {
        for (Activity activity : activitys)
            if (activity.getClass().equals(cls))
            {
                finishSingleActivity(activity);
                break;
            }
    }
}
