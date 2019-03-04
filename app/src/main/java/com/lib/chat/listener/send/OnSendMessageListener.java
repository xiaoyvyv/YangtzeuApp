package com.lib.chat.listener.send;

import com.lib.chat.listener.receive.OnReceiveServerMessageListener;

public interface OnSendMessageListener<T> extends OnReceiveServerMessageListener {
    void onSending(T t);

    void onFailure(String info);
}
