package com.yangtzeu.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ServiceUtils;
import com.blankj.utilcode.util.Utils;
import com.yangtzeu.R;
import com.yangtzeu.entity.OnLineBean;
import com.yangtzeu.listener.OnResultListener;
import com.yangtzeu.ui.activity.MainActivity;
import com.yangtzeu.utils.YangtzeuUtils;

import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

public class BackgroundService extends Service {
    private Timer timer;
    private Timer timer1;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate() {
        super.onCreate();

        timer = new Timer();
        //保持教务处服务器连接
        final Handler keepHandler = new Handler(getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                //保持教务处服务器连接
                YangtzeuUtils.getStudentInfo();
            }
        };

        timer1 = new Timer();
        //保持App服务器连接
        final Handler keepHandler1 = new Handler(getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                YangtzeuUtils.keepOnline(new OnResultListener<OnLineBean>() {
                    @Override
                    public void onResult(OnLineBean s) {
                        int size = s.getSize();
                        SPUtils.getInstance("app_info").put("online_size", size);
                        Intent intent = new Intent();
                        intent.setAction("Online_BroadcastReceiver");
                        intent.putExtra("online_size", size);
                        sendBroadcast(intent);
                    }
                });
            }
        };

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Message msg = Message.obtain();
                keepHandler.sendMessage(msg);
            }
        }, 0, 30000L);

        timer1.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                boolean appForeground = AppUtils.isAppForeground();
                if (appForeground) {
                    Message msg = Message.obtain();
                    keepHandler1.sendMessage(msg);
                }
            }
        }, 0, 60000L);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startForeground(this);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
        }
        if (timer1 != null) {
            timer1.cancel();
        }
        stopForeground(true);
        LogUtils.i("结束后台服务");
    }

    public static void startForeground(Service service) {
        NotificationManager mNotificationManager = (NotificationManager) service.getSystemService(Context.NOTIFICATION_SERVICE);
        // 通知渠道的id
        String id = "CHANNEL_ID";
        // 用户可以看到的通知渠道的名字.
        CharSequence name = service.getString(R.string.background_server);
        //用户可以看到的通知渠道的描述
        String description = service.getString(R.string.background_server_description);

        NotificationChannel mChannel ;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int  importance = NotificationManager.IMPORTANCE_HIGH;
            mChannel = new NotificationChannel(id, name, importance);
            //配置通知渠道的属性
            mChannel.setDescription(description);
            //设置通知出现时的闪灯（如果 android 设备支持的话）
            mChannel.enableLights(false);
            mChannel.setLightColor(Color.RED);
            //设置通知出现时的震动（如果 android 设备支持的话）
            mChannel.enableVibration(false);
            mChannel.setVibrationPattern(new long[]{100});
            //最后在notificationmanager中创建该通知渠道 //
            mNotificationManager.createNotificationChannel(mChannel);
        }

        // 为该通知设置一个id
        int notifyID = 8888;
        // 通知渠道的id
        String CHANNEL_ID = "CHANNEL_ID";
        Notification.Builder build = new Notification.Builder(service);
        build.setContentTitle("数据连接服务").setContentText("教务处数据连接服务");
        build.setSmallIcon(R.mipmap.ic_launcher);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            build.setChannelId(CHANNEL_ID);
        }
        Notification notification = build.build();
        service.startForeground(notifyID, notification);
    }

    //结束后台服务
    public static void stop(Context context) {
        Intent intent = new Intent(context, BackgroundService.class);
        context.stopService(intent);
    }
}
