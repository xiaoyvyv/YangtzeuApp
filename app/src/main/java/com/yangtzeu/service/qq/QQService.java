package com.yangtzeu.service.qq;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;

public class QQService extends AccessibilityService {
    private boolean IsVisitorsActivity_ListView = false;
    private boolean IsFriendProfileCardActivity = false;
    private static final String VisitorsActivity = "com.tencent.mobileqq.activity.VisitorsActivity";
    private static final String FriendProfileCardActivity = "com.tencent.mobileqq.activity.FriendProfileCardActivity";
    private static final String TAG = "QQLikeService";

    public QQService() {
        LogUtils.e(TAG, "辅助功能即将打开");
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        String className = accessibilityEvent.getClassName().toString();
        //获取根节点
        AccessibilityNodeInfo nodeInfo_Root = getRootInActiveWindow();
        if (className.contains("com.tencent.mobileqq")) {
            //判断是否是规定的Activity
            switch (className) {
                case VisitorsActivity:
                    IsVisitorsActivity_ListView = true;
                    IsFriendProfileCardActivity = false;
                    break;
                case FriendProfileCardActivity:
                    IsVisitorsActivity_ListView = false;
                    IsFriendProfileCardActivity = true;
                    break;
                default:
                    IsVisitorsActivity_ListView = false;
                    IsFriendProfileCardActivity = false;
                    break;
            }
        } else if (className.contains("android")) {
            WhichWindow(nodeInfo_Root);
        } else {
            IsVisitorsActivity_ListView = false;
            IsFriendProfileCardActivity = false;
        }
    }

    private void WhichWindow(AccessibilityNodeInfo accessibilityNodeInfo) {
        if (IsVisitorsActivity_ListView) {
            //QQ点赞-----窗口
            SPUtils.getInstance("Main_Switch").put("Both_Witch", "2");
            new Window_Like(this, accessibilityNodeInfo);
        } else if (IsFriendProfileCardActivity) {
            //QQ名片-----窗口
            SPUtils.getInstance("Main_Switch").put("Both_Witch", "4");
            new Window_Friend(this, accessibilityNodeInfo);
        } else {
            //非指定-----窗口
            SPUtils.getInstance("Main_Switch").put("Both_Witch", "0");
        }
    }

    @Override
    public void onInterrupt() {

    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        LogUtils.e("辅助功能已连接");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    try {
                        Intent intent = QQService.this.getPackageManager().getLaunchIntentForPackage("com.tencent.mobileqq");
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
        LogUtils.e("辅助功能已断开");
        return super.onUnbind(intent);
    }
}
