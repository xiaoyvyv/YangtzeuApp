package com.lib.chat.listener.handler;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;

import com.blankj.utilcode.util.Utils;
import com.lib.chat.common.UserManager;
import com.lib.chat.listener.receive.OnReceiveGroupMessageListener;
import com.lib.chat.listener.receive.OnReceiveMessageListener;
import com.lib.chat.listener.receive.OnReceiveServerMessageListener;
import com.lib.chat.listener.receive.OnReceiveUnlimitGroupMessageListener;
import com.xiaomi.mimc.MIMCGroupMessage;
import com.xiaomi.mimc.MIMCMessage;
import com.xiaomi.mimc.MIMCMessageHandler;
import com.xiaomi.mimc.MIMCServerAck;

import java.util.List;

public
class MessageHandler implements MIMCMessageHandler {
    private OnReceiveMessageListener messageListener;
    private OnReceiveGroupMessageListener groupMessageListener;
    private OnReceiveUnlimitGroupMessageListener unlimitGroupMessageListener;
    private OnReceiveServerMessageListener serverMessageListener;
    private Handler uiHandler;

    public MessageHandler(OnReceiveMessageListener messageListener, OnReceiveGroupMessageListener groupMessageListener
            , OnReceiveUnlimitGroupMessageListener unlimitGroupMessageListener, OnReceiveServerMessageListener serverMessageListener) {
        this.messageListener = messageListener;
        this.groupMessageListener = groupMessageListener;
        this.unlimitGroupMessageListener = unlimitGroupMessageListener;
        this.serverMessageListener = serverMessageListener;
        uiHandler = new Handler(Looper.getMainLooper());
    }

    /**
     * 接收单聊消息
     * MIMCMessage类
     * String packetId 消息ID
     * long sequence 序列号
     * String fromAccount 发送方帐号
     * String toAccount 接收方帐号
     * byte[] payload 消息体
     * long timestamp 时间戳
     */
    @Override
    public void handleMessage(List<MIMCMessage> packets) {
        for (int i = 0; i < packets.size(); ++i) {
            final MIMCMessage mimcMessage = packets.get(i);
            if (messageListener != null) {
                uiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        messageListener.onMessageSuccess(mimcMessage);
                    }
                });
            }
        }

        MessageHandler.sendReceiveMessageBroadcast();
    }

    /**
     * 接收群聊消息
     * MIMCGroupMessage类
     * String packetId 消息ID
     * long groupId 群ID
     * long sequence 序列号
     * String fromAccount 发送方帐号
     * byte[] payload 消息体
     * long timestamp 时间戳
     */
    @Override
    public void handleGroupMessage(List<MIMCGroupMessage> packets) {
        for (int i = 0; i < packets.size(); i++) {
            final MIMCGroupMessage mimcGroupMessage = packets.get(i);
            if (groupMessageListener != null) {
                uiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        groupMessageListener.onMessageSuccess(mimcGroupMessage);
                        //回调完后立即取消监听
                        UserManager.getInstance().setServerMessageListener(null);
                    }
                });
            }
        }

        MessageHandler.sendReceiveMessageBroadcast();
    }

    /**
     * 接收无限大群聊消息
     * MIMCGroupMessage类
     * String packetId 消息ID
     * long groupId 群ID
     * long sequence 序列号
     * String fromAccount 发送方帐号
     * byte[] payload 消息体
     * long timestamp 时间戳
     */
    @Override
    public void handleUnlimitedGroupMessage(List<MIMCGroupMessage> packets) {
        for (int i = 0; i < packets.size(); i++) {
            final MIMCGroupMessage mimcGroupMessage = packets.get(i);
            if (unlimitGroupMessageListener != null) {
                uiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        unlimitGroupMessageListener.onMessageSuccess(mimcGroupMessage);
                    }
                });
            }
        }

        MessageHandler.sendReceiveMessageBroadcast();
    }

    /**
     * 接收服务端已收到发送消息确认
     * MIMCServerAck类
     * String packetId 消息ID
     * long sequence 序列号
     * long timestamp 时间戳
     */
    @Override
    public void handleServerAck(final MIMCServerAck serverAck) {
        if (serverMessageListener != null) {
            uiHandler.post(new Runnable() {
                @Override
                public void run() {
                    serverMessageListener.onServerAck(serverAck);
                }
            });
        }
    }

    /**
     * 接收单聊超时消息
     *
     * @param message 单聊消息类
     */
    @Override
    public void handleSendMessageTimeout(final MIMCMessage message) {
        if (messageListener != null) {
            uiHandler.post(new Runnable() {
                @Override
                public void run() {
                    messageListener.onMessageTimeout(message);
                }
            });
        }
    }

    /**
     * 接收群聊超时消息
     *
     * @param groupMessage 群聊消息类
     */
    @Override
    public void handleSendGroupMessageTimeout(final MIMCGroupMessage groupMessage) {
        if (groupMessageListener != null) {
            uiHandler.post(new Runnable() {
                @Override
                public void run() {
                    groupMessageListener.onMessageTimeout(groupMessage);
                }
            });
        }
    }

    /**
     * 接收无限大群聊超时消息
     *
     * @param mimcGroupMessage 无限大群聊消息类
     */
    @Override
    public void handleSendUnlimitedGroupMessageTimeout(final MIMCGroupMessage mimcGroupMessage) {
        if (unlimitGroupMessageListener != null) {
            uiHandler.post(new Runnable() {
                @Override
                public void run() {
                    unlimitGroupMessageListener.onMessageTimeout(mimcGroupMessage);
                }
            });
        }
    }

    public static void sendReceiveMessageBroadcast() {
        Intent intent = new Intent();
        intent.setAction("ReceiveMessage_BroadcastReceiver");
        Utils.getApp().sendBroadcast(intent);
    }

}
