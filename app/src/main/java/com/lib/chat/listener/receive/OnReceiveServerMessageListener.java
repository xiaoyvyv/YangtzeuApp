package com.lib.chat.listener.receive;

import com.xiaomi.mimc.MIMCServerAck;


public interface OnReceiveServerMessageListener {
    void onServerAck(MIMCServerAck serverAck);
}