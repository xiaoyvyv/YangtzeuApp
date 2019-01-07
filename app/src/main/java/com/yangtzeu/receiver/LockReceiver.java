package com.yangtzeu.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.Utils;
import com.yangtzeu.model.LockModel;

public class LockReceiver extends BroadcastReceiver {
    public static final String ACTION = "com.yangtzeu.service.LockReceiver";

    public LockReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_USER_PRESENT);
        Utils.getApp().registerReceiver(this, filter);
    }

    @Override
    public void onReceive(final Context context, Intent intent) {
        if (StringUtils.equals(intent.getAction(), ACTION) ||
                StringUtils.equals(intent.getAction(), Intent.ACTION_SCREEN_ON) ||
                StringUtils.equals(intent.getAction(), Intent.ACTION_SCREEN_OFF) ||
                StringUtils.equals(intent.getAction(), Intent.ACTION_USER_PRESENT)) {
            new LockModel().checkIsLockFinish(context.getApplicationContext(), LockReceiver.this, intent.getAction());
        }
    }

}
