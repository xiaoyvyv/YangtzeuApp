package com.lib.x5web;

import android.content.Context;
import android.os.Handler;
import android.webkit.JavascriptInterface;

import com.blankj.utilcode.util.StringUtils;
import com.lib.chat.common.Constant;
import com.yangtzeu.utils.MyUtils;

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
                    MyUtils.chatOnline(context,number, Constant.USER_TYPE_USER);
                }
            }
        });
    }
}
