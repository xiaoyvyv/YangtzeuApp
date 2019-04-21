package com.yangtzeu.app;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.multidex.MultiDex;

import com.blankj.utilcode.util.CrashUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.Utils;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.lib.chat.bean.PayLoadBean;
import com.lib.chat.common.UserManager;
import com.lib.chat.listener.receive.OnReceiveGroupMessageListener;
import com.lib.chat.listener.receive.OnReceiveMessageListener;
import com.lib.chat.listener.receive.OnReceiveUnlimitGroupMessageListener;
import com.lib.subutil.GsonUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.tencent.smtt.sdk.QbSdk;
import com.xiaomi.mimc.MIMCGroupMessage;
import com.xiaomi.mimc.MIMCMessage;
import com.yangtzeu.ui.activity.ChatActivity;
import com.yangtzeu.ui.activity.LoginActivity;
import com.yangtzeu.utils.ALiOssUtils;
import com.yangtzeu.utils.GoogleUtils;
import com.yangtzeu.utils.NotificationUtils;

public class MyApplication extends Application {

    static {

        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @NonNull
            @Override
            public RefreshHeader createRefreshHeader(@NonNull Context context, @NonNull RefreshLayout layout) {
                return new ClassicsHeader(context);
            }
        });

        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @NonNull
            @Override
            public RefreshFooter createRefreshFooter(@NonNull Context context, @NonNull RefreshLayout layout) {
                return new ClassicsFooter(context);
            }
        });
    }


    @SuppressLint("SdCardPath")
    @Override
    public void onCreate() {
        super.onCreate();

        GoogleUtils.DEBUG = false;
        GoogleUtils.init(this);

        //初始化AndroidUtils
        Utils.init(this);
        //初始化X5内核
        initX5Web(this);
        //初始化阿里云Oss
        ALiOssUtils.initOSSClient(this);

        //捕获全局异常
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            CrashUtils.init("/sdcard/A_Tool/Yangtzeu_Crash/", new CrashUtils.OnCrashListener() {
                @Override
                public void onCrash(String crashInfo, Throwable e) {
                    restartApp(crashInfo, e);
                }
            });
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    private void initX5Web(Context context) {
        //x5内核初始化接口
        QbSdk.initX5Environment(context, new QbSdk.PreInitCallback() {
            @Override
            public void onViewInitFinished(boolean arg0) {
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                LogUtils.i("X5内核启动结果：" + arg0);
            }

            @Override
            public void onCoreInitFinished() {

            }
        });
    }

    /**
     * 重启应用
     */
    private void restartApp(String crashInfo, Throwable e) {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        intent.putExtra("isCrash", true);
        intent.putExtra("crashCause", e.getCause().toString());
        intent.putExtra("crashMessage", crashInfo);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        //再此之前可以做些退出等操作
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    public static void setMessListener() {
        //聊天系统注册
        UserManager instance = UserManager.getInstance();
        //私聊
        instance.setMessageListener(new OnReceiveMessageListener() {
            @Override
            public void onMessageSuccess(MIMCMessage mimcMessage) {
                try {
                    LogUtils.e("接收到私聊消息", GsonUtils.toJson(mimcMessage));
                    //消息内容
                    String payload = new String(mimcMessage.getPayload());

                    //解析payload包含内容
                    String message;
                    Gson GSON = new Gson();
                    try {
                        PayLoadBean payLoadBean = GSON.fromJson(payload, PayLoadBean.class);
                        message = payLoadBean.getText();
                    } catch (JsonSyntaxException e) {
                        message = payload;
                    }


                    String fromAccount = mimcMessage.getFromAccount();
                    NotificationUtils notificationUtils = new NotificationUtils(Utils.getApp());

                    Intent intent = new Intent(Utils.getApp(), ChatActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                    notificationUtils.sendNotification("用户：" + fromAccount, message, intent);

                    if (!StringUtils.isEmpty(fromAccount))
                        UserManager.getInstance().setUnRead(fromAccount, 1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


            @Override
            public void onMessageTimeout(MIMCMessage mimcMessage) {
                LogUtils.e(mimcMessage.getFromAccount() + "接收失败");
            }
        });
        //群聊
        instance.setGroupMessageListener(new OnReceiveGroupMessageListener() {
            @Override
            public void onMessageSuccess(MIMCGroupMessage mimcGroupMessage) {
                try {
                    LogUtils.e("接收到群聊消息", GsonUtils.toJson(mimcGroupMessage));
                    //消息内容
                    String payload = new String(mimcGroupMessage.getPayload());

                    //解析payload包含内容
                    String message;
                    Gson GSON = new Gson();
                    try {
                        PayLoadBean payLoadBean = GSON.fromJson(payload, PayLoadBean.class);
                        message = payLoadBean.getText();
                    } catch (JsonSyntaxException e) {
                        message = payload;
                    }
                    String topicId = String.valueOf(mimcGroupMessage.getTopicId());


                    boolean isHide = SPUtils.getInstance("group_notice").getBoolean(topicId, false);
                    if (!isHide) {
                        NotificationUtils notificationUtils = new NotificationUtils(Utils.getApp());
                        Intent intent = new Intent(Utils.getApp(), ChatActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                        notificationUtils.sendNotification("群：" + topicId, message, intent);
                    }
                    if (!StringUtils.isEmpty(topicId))
                        UserManager.getInstance().setUnRead(topicId, 1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onMessageTimeout(MIMCGroupMessage mimcGroupMessage) {
                LogUtils.e(mimcGroupMessage.getFromAccount() + "接收失败");
            }
        });
        //无限大
        instance.setUnlimitGroupMessageListener(new OnReceiveUnlimitGroupMessageListener() {
            @Override
            public void onMessageSuccess(MIMCGroupMessage mimcGroupMessage) {
                LogUtils.e("接收到大群聊消息", GsonUtils.toJson(mimcGroupMessage));
                try {
                    LogUtils.e("接收到大群聊消息", GsonUtils.toJson(mimcGroupMessage));
                    //消息内容
                    String payload = new String(mimcGroupMessage.getPayload());

                    //解析payload包含内容
                    String message;
                    Gson GSON = new Gson();
                    try {
                        PayLoadBean payLoadBean = GSON.fromJson(payload, PayLoadBean.class);
                        message = payLoadBean.getText();
                    } catch (JsonSyntaxException e) {
                        message = payload;
                    }
                    String topicId = String.valueOf(mimcGroupMessage.getTopicId());

                    boolean isHide = SPUtils.getInstance("group_notice").getBoolean(topicId, false);
                    if (!isHide) {
                        NotificationUtils notificationUtils = new NotificationUtils(Utils.getApp());
                        Intent intent = new Intent(Utils.getApp(), ChatActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                        notificationUtils.sendNotification("大群：" + topicId, message, intent);
                    }

                    if (!StringUtils.isEmpty(topicId))
                        UserManager.getInstance().setUnRead(topicId, 1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onMessageTimeout(MIMCGroupMessage mimcGroupMessage) {
                LogUtils.e(mimcGroupMessage.getFromAccount() + "接收失败");
            }

        });
    }
}
