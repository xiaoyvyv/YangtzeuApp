package com.lib.chat.listener;

import android.content.Intent;

import com.blankj.utilcode.util.Utils;
import com.xiaomi.mimc.MIMCOnlineStatusListener;
import com.xiaomi.mimc.common.MIMCConstant;

/**
 * 用户在线状态监听
 */
public class OnlineStatusListener implements MIMCOnlineStatusListener {
    @Override
    public void statusChange(MIMCConstant.OnlineStatus status, String errType, String errReason, String errDescription) {
        Intent intent = new Intent();
        intent.setAction("MIMC_Online_BroadcastReceiver");
        if (status == MIMCConstant.OnlineStatus.ONLINE) {
            intent.putExtra("online_status", true);
        } else {
            intent.putExtra("online_status", false);
        }
        Utils.getApp().sendBroadcast(intent);
    }
}
