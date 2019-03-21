package com.yangtzeu.service.qq;

import android.content.Context;
import android.view.accessibility.AccessibilityNodeInfo;

import com.blankj.utilcode.util.LogUtils;


/**
 * Created by 2016 on 2018/1/1.
 */

class Window_Friend {
    private Context context;
    Window_Friend(Context context, AccessibilityNodeInfo NodeInfo) {
        this.context = context;
        SetUp();
    }
    private void SetUp() {
        LogUtils.e("QQ_Window:", "---------------------------QQ名片窗口-------------------------");


    }
}
