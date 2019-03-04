package com.lib.chat.listener.handler;


import com.xiaomi.mimc.MIMCUnlimitedGroupHandler;

public class UnlimitedGroupHandler implements MIMCUnlimitedGroupHandler {

    //创建无限大群消息回调
    @Override
    public void handleCreateUnlimitedGroup(long topicId, String topicName, boolean success, String errMsg, Object obj) {

    }

    //加入无限大群消息回调
    @Override
    public void handleJoinUnlimitedGroup(long topicId, int code, String errMsg, Object obj) {

    }

    //离开无限大群消息回调
    @Override
    public void handleQuitUnlimitedGroup(long topicId, int code, String errMsg, Object obj) {

    }

    //解散无限大群消息回调
    @Override
    public void handleDismissUnlimitedGroup(long topicId, int code, String errMsg, Object obj) {

    }
}
