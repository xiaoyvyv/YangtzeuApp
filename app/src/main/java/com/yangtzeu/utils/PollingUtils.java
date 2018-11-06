package com.yangtzeu.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.SystemClock;

import java.util.Objects;

public class PollingUtils {
    //开启轮询服务
    public static void startPollingBroadcast(Context context, int seconds, Class<?> cls, String action) {
        //获取AlarmManager系统服务
        AlarmManager manager = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);

        //包装需要执行Service的Intent
        Intent intent = new Intent(context, cls);
        intent.setAction(action);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (manager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                manager.setExactAndAllowWhileIdle(AlarmManager.RTC, System.currentTimeMillis()+seconds, pendingIntent);
            } else{
                manager.setExact(AlarmManager.RTC, System.currentTimeMillis()+seconds, pendingIntent);
            }
        }
    }

    //停止轮询服务
    public static void stopPollingBroadcast(Context context, Class<?> cls, String action) {
        AlarmManager manager = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, cls);
        intent.setAction(action);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        //取消正在执行的服务
        Objects.requireNonNull(manager).cancel(pendingIntent);
    }
}