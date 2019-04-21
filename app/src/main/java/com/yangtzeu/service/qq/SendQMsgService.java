package com.yangtzeu.service.qq;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityWindowInfo;

import com.blankj.utilcode.util.LogUtils;

import java.util.List;

public class SendQMsgService extends AccessibilityService {

    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        String className = accessibilityEvent.getClassName().toString();
        String packageName = accessibilityEvent.getPackageName().toString();
        LogUtils.e("监听到包名：" + packageName, "监听到事件类名：" + className);

        List<AccessibilityWindowInfo> windows = getWindows();
        AccessibilityNodeInfo nameView = AccessibilityUtils.findViewByText(windows, "小玉");
        AccessibilityNodeInfo sendBtn = AccessibilityUtils.findViewByText(windows, "发送");
        AccessibilityNodeInfo editText = AccessibilityUtils.findEdiText(windows);

        if (nameView != null) {
            if (editText != null) {
                AccessibilityUtils.setText(editText, "测试");
            }
            if (sendBtn != null) {
                sendBtn.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            }
        }
    }

    @Override
    public void onInterrupt() {

    }


    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        LogUtils.e("QQ连发辅助功能已连接");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    try {
                        Intent intent = SendQMsgService.this.getPackageManager().getLaunchIntentForPackage("com.tencent.mobileqq");
                        startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        LogUtils.e("QQ连发辅助功能已断开");
        return super.onUnbind(intent);
    }
}
