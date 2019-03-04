package com.yangtzeu.utils;

import android.annotation.TargetApi;
import android.app.AppOpsManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.Build;

import com.blankj.utilcode.util.Utils;
import com.yangtzeu.R;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;


/**
 * Created by LaoZhao on 2017/11/19.
 */

public class NotificationUtils extends ContextWrapper {

    private NotificationManager manager;
    public String id = "channel_1";
    public String name = "新长大助手提示通知（重要）";

    public NotificationUtils(Context context) {
        super(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void createNotificationChannel() {
        NotificationChannel channel = new NotificationChannel(id, name, NotificationManager.IMPORTANCE_HIGH);
        getManager().createNotificationChannel(channel);
    }

    public NotificationManager getManager() {
        if (manager == null) {
            manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        }
        return manager;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Notification.Builder getChannelNotification(String title, String content, Intent intent) {
        Notification.Builder builder = new Notification.Builder(getApplicationContext(), id)
                .setContentTitle(title)
                .setContentText(content)
                .setWhen(System.currentTimeMillis())
                .setPriority(Notification.PRIORITY_LOW)
                .setSmallIcon(R.drawable.ic_message)
                .setDefaults(Notification.DEFAULT_LIGHTS)
                .setCategory(Notification.CATEGORY_MESSAGE)
                .setVisibility(Notification.VISIBILITY_PRIVATE)
                .setAutoCancel(true);
        if (intent != null) {
            builder.setContentIntent(PendingIntent.getActivity(getApplicationContext(), 0, intent, 0));
        }
        return builder;
    }

    public NotificationCompat.Builder getNotification_25(String title, String content, Intent intent) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext())
                .setContentTitle(title)
                .setWhen(System.currentTimeMillis())
                .setContentText(content)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setPriority(Notification.PRIORITY_HIGH)
                .setSmallIcon(R.drawable.ic_message)
                .setCategory(Notification.CATEGORY_MESSAGE)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setAutoCancel(true);
        if (intent != null) {
            builder.setContentIntent(PendingIntent.getActivity(getApplicationContext(), 0, intent, 0));
        }
        return builder;
    }

    public Notification getNotification(String title, String content, Intent intent) {
        if (Build.VERSION.SDK_INT >= 26) {
            return getChannelNotification(title, content, intent).build();
        } else {
            return getNotification_25(title, content, intent).build();
        }
    }

    public void sendNotification(String title, String content, Intent intent) {
        if (Build.VERSION.SDK_INT >= 26) {
            createNotificationChannel();
            Notification notification = getChannelNotification(title, content, intent).build();
            getManager().notify(1, notification);
            MyUtils.mVibrator(Utils.getApp(), 300);

        } else {
            Notification notification = getNotification_25(title, content, intent).build();
            getManager().notify(1, notification);
            MyUtils.mVibrator(Utils.getApp(), 300);

        }
    }

    /**
     * 检查通知权限，未获取则跳到通知栏设置界面
     * * @param context
     */
    public static void toNotificationSetting(Context context) {
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
        localIntent.setData(Uri.fromParts("package", context.getPackageName(), null));
        context.startActivity(localIntent);
    }


    /**
     * 获取通知权限
     * * @param context
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static boolean isNotificationEnabled(Context context) {
        String CHECK_OP_NO_THROW = "checkOpNoThrow";
        String OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION";
        AppOpsManager mAppOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        ApplicationInfo appInfo = context.getApplicationInfo();
        String pkg = context.getApplicationContext().getPackageName();
        int uid = appInfo.uid;
        Class appOpsClass;
        try {
            appOpsClass = Class.forName(AppOpsManager.class.getName());
            Method checkOpNoThrowMethod = appOpsClass.getMethod(CHECK_OP_NO_THROW, Integer.TYPE, Integer.TYPE, String.class);
            Field opPostNotificationValue = appOpsClass.getDeclaredField(OP_POST_NOTIFICATION);
            int value = (Integer) opPostNotificationValue.get(Integer.class);
            return ((Integer) checkOpNoThrowMethod.invoke(mAppOps, value, uid, pkg) == AppOpsManager.MODE_ALLOWED);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


}