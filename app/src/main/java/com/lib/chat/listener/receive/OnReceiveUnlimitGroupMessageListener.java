package com.lib.chat.listener.receive;

import com.xiaomi.mimc.MIMCGroupMessage;


public interface OnReceiveUnlimitGroupMessageListener {
    void onMessageSuccess(MIMCGroupMessage mimcGroupMessage);

    void onMessageTimeout(MIMCGroupMessage mimcGroupMessage);
}