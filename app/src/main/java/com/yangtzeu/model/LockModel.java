package com.yangtzeu.model;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import com.blankj.utilcode.util.CacheDiskUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.android.material.textfield.TextInputEditText;
import com.yangtzeu.R;
import com.yangtzeu.model.imodel.ILockModel;
import com.yangtzeu.receiver.LockReceiver;
import com.yangtzeu.ui.view.LockView;
import com.yangtzeu.utils.DeviceUtils;
import com.yangtzeu.utils.MyUtils;
import com.yangtzeu.utils.NotificationUtils;
import com.yangtzeu.utils.PollingUtils;
import com.yangtzeu.utils.YangtzeuUtils;

import java.util.Objects;

public class LockModel implements ILockModel {

    @Override
    public void startLockPhone(Activity activity, LockView view) {
        //进程不同，用文件存储数据，以便时实更新
        CacheDiskUtils.getInstance(MyUtils.geCacheDir()).put("lock_time", String.valueOf(TimeUtils.getNowMills() + view.getLockTimeMill()));
        Intent intent = new Intent(activity, LockReceiver.class);
        intent.setAction(LockReceiver.ACTION);
        activity.sendBroadcast(intent);

        PollingUtils.startPollingBroadcast(activity, 10000, LockReceiver.class, LockReceiver.ACTION);
    }

    @Override
    public void setLockTime(final Activity activity, final LockView lockView) {
        @SuppressLint("InflateParams") final View view = activity.getLayoutInflater().inflate(R.layout.view_input_lock, null);
        final TextInputEditText timeView = view.findViewById(R.id.timeView);
        final AlertDialog dialog = MyUtils.getAlert(activity, null, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String time = Objects.requireNonNull(timeView.getText()).toString().trim();
                final long lock_long = Long.parseLong(time) * 60 * 1000;
                if (StringUtils.isEmpty(time)) {
                    ToastUtils.showShort(R.string.please_input);
                } else {
                    AlertDialog dialogConf = MyUtils.getAlert(activity, "请在合理的时间使用本功能！\n\n" + "到达指定时间后(误差在一分钟内)手机会长震动，即可解锁！" +
                            "\n\n手机将锁定：" + time + "分钟\n\n解锁时间：" + TimeUtils.millis2String(TimeUtils.getNowMills() + lock_long), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            lockView.setLockTimeMill(lock_long);
                            startLockPhone(activity, lockView);
                        }
                    });
                    dialogConf.show();
                    dialogConf.setCancelable(false);
                    dialogConf.setCanceledOnTouchOutside(false);
                }
            }
        });
        dialog.setTitle(null);
        dialog.setView(view);
        dialog.show();
    }

    public void checkIsLockFinish(final Context context, BroadcastReceiver broadcastReceiver, String action) {
        //联网检查白名单
        YangtzeuUtils.getLockPhoneWhiteUser();

        final DeviceUtils deviceUtils = new DeviceUtils(context);

        //进程不同，用文件存储数据，以便时实更新
        long unlock = Long.parseLong(CacheDiskUtils.getInstance(MyUtils.geCacheDir()).getString("lock_time", "0"));
        long nowMills = TimeUtils.getNowMills();
        boolean isFinish = nowMills >= unlock;
        if (isFinish) {
            //如果是Activity的检查上次锁定是否完成，则不提醒，因为大部分都完成了
            if (!action.equals("ACTION_SCREEN_CHECK")) {
                MyUtils.mVibrator(context, 3000);
                LogUtils.e("Action：" + action, "锁定完成：" + unlock, nowMills);
                //发送通知
                NotificationUtils notificationUtils = new NotificationUtils(context);
                notificationUtils.sendNotification("新长大助手远离手机", "手机锁定时间完成", null);

                //停止锁屏服务和监听
                if (broadcastReceiver != null)
                    context.unregisterReceiver(broadcastReceiver);
                PollingUtils.stopPollingBroadcast(context, LockReceiver.class, LockReceiver.ACTION);
            }
        } else {
            LogUtils.e("Action：" + action, "锁定还有" + ((unlock - nowMills) / 1000) + "秒");
            //如果用户是点亮屏幕，3s后锁屏|否则，直接锁屏
            switch (action) {
                case Intent.ACTION_SCREEN_ON:
                    NotificationUtils notificationUtils = new NotificationUtils(context);
                    notificationUtils.sendNotification("新长大助手远离手机", "锁定未完成，还有" + ((unlock - nowMills) / 1000) + "秒，即将关屏", null);
                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            deviceUtils.LockNow();
                            PollingUtils.startPollingBroadcast(context, 10000, LockReceiver.class, LockReceiver.ACTION);
                        }
                    }, 3000);
                    break;
                case "ACTION_SCREEN_CHECK":
                    //如果是 ACTION_SCREEN_CHECK ，则提示一个对话框
                    final AlertDialog show = new AlertDialog.Builder(context).setMessage("上次锁定未完成，还有" + ((unlock - nowMills) / 1000) + "秒，即将关屏").create();
                    show.show();
                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            show.dismiss();
                            deviceUtils.LockNow();
                            PollingUtils.startPollingBroadcast(context, 10000, LockReceiver.class, LockReceiver.ACTION);
                        }
                    }, 3000);
                    break;
                default:
                    deviceUtils.LockNow();
                    PollingUtils.startPollingBroadcast(context, 10000, LockReceiver.class, LockReceiver.ACTION);
                    break;
            }
        }
    }
}
