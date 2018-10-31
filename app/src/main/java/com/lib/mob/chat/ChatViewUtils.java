package com.lib.mob.chat;

import android.content.Context;

import com.blankj.utilcode.util.LogUtils;
import com.mob.imsdk.model.IMUser;
import com.yangtzeu.listener.OnResultListener;

public class ChatViewUtils {


    public static void IMUser2ChatUser(Context context, IMUser imUser, final OnResultListener<ChatUser> listener) {
        //设置对方的 User
        final String id = imUser.getId();
        final String nickname = imUser.getNickname();
        String avatar = imUser.getAvatar();
        LogUtils.e(id, nickname, avatar);
        ChatUser chatUser = new ChatUser(id, nickname, avatar);
        listener.onResult(chatUser);
    }
}
