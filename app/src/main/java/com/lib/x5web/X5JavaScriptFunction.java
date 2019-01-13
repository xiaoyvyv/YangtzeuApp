package com.lib.x5web;

import android.content.Context;
import android.os.Handler;
import android.webkit.JavascriptInterface;

import com.blankj.utilcode.util.StringUtils;
import com.lib.mob.im.IMManager;
import com.mob.imsdk.model.IMConversation;

public class X5JavaScriptFunction {
    private final Context context;

    public X5JavaScriptFunction(Context context) {
        this.context = context;
    }
    @JavascriptInterface
    public void openChat(final String number) {
        new Handler(context.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                if (!StringUtils.isEmpty(number)) {
                    //targetId - 目标id（群聊为群的id，私聊为对方id）
                    IMManager.chat(number,IMConversation.TYPE_USER);
                }
            }
        });
    }
}
