package com.yangtzeu.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Build;

import com.yangtzeu.R;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

/**
 * Created by LaoZhao on 2017/11/19.
 */

public class NotificationUtils extends ContextWrapper {

    private NotificationManager manager;
    public static final String id = "channel_1";
    public static final String name = "channel_name_1";

    public NotificationUtils(Context context) {
        super(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void createNotificationChannel() {
        NotificationChannel channel = new NotificationChannel(id, name, NotificationManager.IMPORTANCE_HIGH);
        getManager().createNotificationChannel(channel);
    }

    private NotificationManager getManager() {
        if (manager == null) {
            manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        }
        return manager;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Notification.Builder getChannelNotification(String title, String content,Intent intent) {
        return new Notification.Builder(getApplicationContext(), id)
                .setContentTitle(title)
                .setContentText(content)
                .setWhen(System.currentTimeMillis())
                .setPriority(Notification.PRIORITY_HIGH)
                .setContentIntent(PendingIntent.getActivity(getApplicationContext(), 0, intent, 0))
                .setSmallIcon(R.drawable.ic_message)
                .setDefaults(Notification.DEFAULT_ALL)
                .setCategory(Notification.CATEGORY_MESSAGE)
                .setAutoCancel(true);
    }

    public NotificationCompat.Builder getNotification_25(String title, String content,Intent intent) {
        return new NotificationCompat.Builder(getApplicationContext())
                .setContentTitle(title)
                .setWhen(System.currentTimeMillis())
                .setContentIntent(PendingIntent.getActivity(getApplicationContext(), 0, intent, 0))
                .setContentText(content)
                .setPriority(Notification.PRIORITY_HIGH)
                .setSmallIcon(R.drawable.ic_message)
                .setCategory(Notification.CATEGORY_MESSAGE)
                .setDefaults(Notification.DEFAULT_ALL)
                .setAutoCancel(true);
    }

    public void sendNotification(String title, String content, Intent intent) {
        if (Build.VERSION.SDK_INT >= 26) {
            createNotificationChannel();
            Notification notification = getChannelNotification(title, content, intent).build();
            getManager().notify(1, notification);
        } else {
            Notification notification = getNotification_25(title, content,intent).build();
            getManager().notify(1, notification);
        }
    }
}