package com.lib.chat.listener.receive;

import com.xiaomi.mimc.MIMCMessage;


public interface OnReceiveMessageListener {
    void onMessageSuccess(MIMCMessage mimcMessage);


    void onMessageTimeout(MIMCMessage mimcMessage);
}