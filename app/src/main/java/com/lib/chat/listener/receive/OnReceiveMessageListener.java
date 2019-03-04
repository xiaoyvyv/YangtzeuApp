package com.lib.chat.listener.receive;

import com.xiaomi.mimc.MIMCMessage;
import com.xiaomi.mimc.MIMCServerAck;


public interface OnReceiveMessageListener {
    void onMessageSuccess(MIMCMessage mimcMessage);


    void onMessageTimeout(MIMCMessage mimcMessage);
}