package com.lib.chat.listener.receive;

import com.xiaomi.mimc.MIMCGroupMessage;


public interface OnReceiveGroupMessageListener {
    void onMessageSuccess(MIMCGroupMessage mimcGroupMessage);


    void onMessageTimeout(MIMCGroupMessage mimcGroupMessage);
}