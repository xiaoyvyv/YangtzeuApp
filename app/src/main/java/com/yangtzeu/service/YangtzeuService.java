package com.yangtzeu.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.yangtzeu.entity.OnLineBean;
import com.yangtzeu.listener.OnResultListener;
import com.yangtzeu.utils.YangtzeuUtils;

import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.Nullable;

/**
 * Created by Administrator on 2018/3/3.
 *
 */

public class YangtzeuService extends Service {
    private TimerTask timeTask;
    private Timer timer;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        timer = new Timer();
        timeTask = new TimerTask() {
            @Override
            public void run() {
                boolean isOnline = SPUtils.getInstance("user_info").getBoolean("online");
                if (isOnline) {
                    YangtzeuUtils.getStudentInfo();
                    YangtzeuUtils.keepOnline(new OnResultListener<OnLineBean>() {
                        @Override
                        public void onResult(OnLineBean s) {
                            int size = s.getSize();
                            SPUtils.getInstance("app_info").put("online_size", size);
                            Intent intent = new Intent();
                            intent.setAction("Online_BroadcastReceiver");
                            intent.putExtra("online_size",size);
                            sendBroadcast(intent);
                        }
                    });
                    LogUtils.i("保持服务器连接");
                }
            }
        };
        timer.schedule(timeTask, 0, 30000);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        if (timer != null) {
            timer.cancel();
        }
        if (timeTask != null) {
            timeTask.cancel();
        }
        super.onDestroy();
    }
}
