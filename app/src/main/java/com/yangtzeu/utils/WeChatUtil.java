package com.yangtzeu.utils;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.service.quicksettings.TileService;
import android.widget.Toast;

import com.lib.yun.StringUtils;
import com.yangtzeu.R;

import java.net.URISyntaxException;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

/**
 * Created by didikee on 2017/7/28.
 * 使用微信捐赠
 */

public class WeChatUtil {
    private static final String WeChatCodePic = "http://whysroom.oss-cn-beijing.aliyuncs.com/yangtzeu/we_chat.png";
    // 微信包名
    private static final String TENCENT_PACKAGE_NAME = "com.tencent.mm";
    // 微信二维码扫描页面地址
    private static final String TENCENT_ACTIVITY_BIZSHORTCUT = "com.tencent.mm.action.BIZSHORTCUT";
    // Extra data
    private static final String TENCENT_EXTRA_ACTIVITY_BIZSHORTCUT = "LauncherUI.From.Scaner.Shortcut";

    /**
     * 启动微信二维码扫描页
     * ps： 需要你引导用户从文件中扫描二维码
     *
     * @param activity activity
     */
    private static void gotoWeChatQrScan(@NonNull Activity activity) {
        Intent intent = new Intent(TENCENT_ACTIVITY_BIZSHORTCUT);
        intent.setPackage(TENCENT_PACKAGE_NAME);
        intent.putExtra(TENCENT_EXTRA_ACTIVITY_BIZSHORTCUT, true);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        try {
            activity.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(activity, "你好像没有安装微信", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * 微信捐赠
     *
     * @param activity   activity
     */
    public static void donateWeiXin(final Activity activity) {
        MyUtils.saveFile(activity,WeChatCodePic,"A_Tool/Donate/","微信付款码.png");
        MyUtils.getAlert(activity, "请等待二维码加载完成后，在微信扫一扫中选择相册里的二维码即可！", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                gotoWeChatQrScan(activity);
            }
        }).show();
    }
}