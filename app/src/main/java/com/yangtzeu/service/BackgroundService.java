package com.yangtzeu.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ServiceUtils;
import com.yangtzeu.entity.OnLineBean;
import com.yangtzeu.listener.OnResultListener;
import com.yangtzeu.utils.YangtzeuUtils;

import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.Nullable;

public class BackgroundService extends Service {
    private Timer timer = new Timer();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        timer = new Timer();
        //保持在线状态
        final Handler keepHandler = new Handler(getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                //保持教务处服务器连接
                YangtzeuUtils.getStudentInfo();

                //保持App服务器连接
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
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
        }
    }

    //结束后台服务
    public static void stop(Context context) {
        Intent intent = new Intent(context, BackgroundService.class);
        context.stopService(intent);
    }
}
