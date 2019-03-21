package com.yangtzeu.service.qq;

import android.content.Context;
import android.view.accessibility.AccessibilityNodeInfo;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;

import java.util.List;

/**
 * Created by 2016 on 2018/1/1.
 *
 */

public class Window_Like {
    protected Context context;
    private AccessibilityNodeInfo NodeInfo;
    Window_Like(Context context, AccessibilityNodeInfo NodeInfo) {
        this.context = context;
        this.NodeInfo = NodeInfo;
        SetUp();
    }
    private void SetUp() {
        String Both_Witch = SPUtils.getInstance( "Main_Switch").getString( "Both_Witch", "0");
        if (Both_Witch.equals("2")) {
            LogUtils.e("QQ_Window:", "---------------------------QQ名片赞窗口-------------------------");
            if (NodeInfo != null) {
                List<AccessibilityNodeInfo> nodes = NodeInfo.findAccessibilityNodeInfosByText("赞");
                for (int i = 0; i < nodes.size(); i++) {
                    String LikeName = nodes.get(i).getClassName().toString();
                    if (LikeName.equals("android.widget.ImageView")) {
                        LogUtils.i("执行自动赞：", "类名为：" + LikeName);
                        if (SPUtils.getInstance("Main_Switch").getString(  "Both_Witch", "0").equals("2")) {
                            for (int j = 0; j < 10; j++) {
                                nodes.get(i).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                            }
                        }
                    }
                }
                LogUtils.i("QQ_Window_LikeWindow", "找到赞按钮集合大小：" + nodes.size());
                List<AccessibilityNodeInfo> back = NodeInfo.findAccessibilityNodeInfosByText("取消");
                for (int i = 0; i < back.size(); i++) {
                    back.get(i).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                }
                LogUtils.i("QQ_Window_LikeWindow", "找到取消按钮集合大小" + back.size());
                NodeInfo.recycle();
            }
        }
    }
}
