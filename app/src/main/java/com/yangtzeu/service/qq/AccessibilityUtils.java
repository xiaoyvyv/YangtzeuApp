package com.yangtzeu.service.qq;

import android.os.Bundle;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityWindowInfo;

import com.blankj.utilcode.util.LogUtils;

import java.util.List;

public class AccessibilityUtils {

    public static AccessibilityNodeInfo findViewByText(List<AccessibilityWindowInfo> windowInfos, String text) {
        AccessibilityNodeInfo temp = null;
        try {
            if (windowInfos != null) {
                for (AccessibilityWindowInfo windowInfo : windowInfos) {
                    //窗口根节点
                    if (windowInfo != null) {
                        AccessibilityNodeInfo nodeInfo = windowInfo.getRoot();
                        if (nodeInfo != null) {
                            List<AccessibilityNodeInfo> findViewNodeInfos = nodeInfo.findAccessibilityNodeInfosByText(text);
                            if (findViewNodeInfos.size() > 0) {
                                temp = findViewNodeInfos.get(0);
                            }
                        }
                    }
                }
            }
            if (temp == null) {
                LogUtils.e("未找到节点-findViewByText：" + text);
            } else {
                LogUtils.e("已找到节点-findViewByText：" + text);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return temp;
    }


    public static AccessibilityNodeInfo findViewById(List<AccessibilityWindowInfo> windowInfos, String id) {
        AccessibilityNodeInfo temp = null;
        try {
            if (windowInfos != null) {
                for (AccessibilityWindowInfo windowInfo : windowInfos) {
                    //窗口根节点
                    if (windowInfo != null) {
                        AccessibilityNodeInfo nodeInfo = windowInfo.getRoot();
                        if (nodeInfo != null) {
                            List<AccessibilityNodeInfo> findViewNodeInfos = nodeInfo.findAccessibilityNodeInfosByViewId(id);
                            if (findViewNodeInfos.size() > 0) {
                                temp = findViewNodeInfos.get(0);
                            }
                        }
                    }
                }
            }
            if (temp == null) {
                LogUtils.e("未找到节点-findViewById：" + id);
            } else {
                LogUtils.e("已找到节点-findViewById：" + id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return temp;
    }

    public static AccessibilityNodeInfo findEdiText(List<AccessibilityWindowInfo> windowInfos) {
        AccessibilityNodeInfo temp = null;
        try {
            if (windowInfos != null) {
                for (AccessibilityWindowInfo windowInfo : windowInfos) {
                    //窗口根节点
                    if (windowInfo != null) {
                        temp = recycleFindEditText(windowInfo.getRoot());
                    }
                }
            }
            if (temp == null) {
                LogUtils.e("未找到节点-findEdiText：EditText");
            } else {
                LogUtils.e("已找到节点-findEdiText：EditText");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return temp;
    }

    private static AccessibilityNodeInfo recycleFindEditText(AccessibilityNodeInfo node) {
        if (node == null) {
            return null;
        }
        try {
            if (node.getChildCount() == 0) {
                if ("android.widget.EditText".contentEquals(node.getClassName())) {
                    return node;
                }
            } else {
                for (int i = 0; i < node.getChildCount(); i++) {
                    if (node.getChild(i) != null) {
                        AccessibilityNodeInfo result = recycleFindEditText(node.getChild(i));
                        if (result != null) {
                            return result;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static void setText(AccessibilityNodeInfo accessibilityNodeInfo, String text) {
        Bundle arguments = new Bundle();
        arguments.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, text);
        if (accessibilityNodeInfo != null)
            accessibilityNodeInfo.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments);
    }
}
