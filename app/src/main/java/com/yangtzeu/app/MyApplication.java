package com.yangtzeu.app;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.CrashUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.Utils;
import com.mob.MobApplication;
import com.mob.imsdk.MobIM;
import com.mob.imsdk.MobIMMessageReceiver;
import com.mob.imsdk.model.IMConversation;
import com.mob.imsdk.model.IMMessage;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.tencent.smtt.sdk.QbSdk;
import com.yangtzeu.http.OkHttp;
import com.yangtzeu.ui.activity.ChatActivity;
import com.yangtzeu.ui.activity.ChatDetailsActivity;
import com.yangtzeu.ui.activity.LoginActivity;
import com.yangtzeu.url.Url;
import com.yangtzeu.utils.ALiOssUtils;
import com.yangtzeu.utils.MyUtils;
import com.yangtzeu.utils.NotificationUtils;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

public class MyApplication extends MobApplication {
    static {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @NonNull
            @Override
            public RefreshHeader createRefreshHeader(@NonNull Context context, @NonNull RefreshLayout layout) {
                return new ClassicsHeader(context);//.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
            }
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @NonNull
            @Override
            public RefreshFooter createRefreshFooter(@NonNull Context context, @NonNull RefreshLayout layout) {
                //指定为经典Footer，默认是 BallPulseFooter
                return new ClassicsFooter(context);
            }
        });
    }

    @Override
    protected String getAppkey() {
        return Url.key;
    }

    @Override
    protected String getAppSecret() {
        return Url.secret;
    }

    @SuppressLint("SdCardPath")
    @Override
    public void onCreate() {
        super.onCreate();
        OkHttp.initOkHttp(this);
        Utils.init(this);
        initX5Web(this);
        initMobReceiver();
        ALiOssUtils.initOSSClient(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            CrashUtils.init("/sdcard/A_Tool/Yangtzeu_Crash/", new CrashUtils.OnCrashListener() {
                @Override
                public void onCrash(String crashInfo, Throwable e) {
                    e.printStackTrace();
                    restartApp(crashInfo, e);
                }
            });
        }
    }

    private void initMobReceiver() {
        MobIM.setOnlySaveOneUserInfo(true);
        MobIM.addMessageReceiver(new MobIMMessageReceiver() {
            @Override
            public void onMessageReceived(List<IMMessage> list) {
                for (IMMessage imMessage : list) {
                    boolean isForbidden = SPUtils.getInstance("user_info").getBoolean("group_notice", false);
                    int type = imMessage.getType();

                    if (type == IMConversation.TYPE_GROUP && isForbidden) {
                        return;
                    }

                    String body = imMessage.getBody();
                    String from = imMessage.getFromUserInfo().getNickname();
                    MyUtils.mVibrator(getApplicationContext(), 500);
                    NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());

                    Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                    notificationUtils.sendNotification(from, body, intent);

                    boolean b = ActivityUtils.getTopActivity() instanceof ChatDetailsActivity;
                    if (!b) {
                        ToastUtils.showLong(from + "给您发消息啦：\n" + body);
                    }
                }
            }

            @Override
            public void onMsgWithDraw(String s, String s1) {

            }
        });
    }

    private void initX5Web(Context context) {
        //搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。
        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {
            @Override
            public void onViewInitFinished(boolean arg0) {
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                LogUtils.i("X5内核启动结果：" + arg0);
            }

            @Override
            public void onCoreInitFinished() {

            }
        };
        //x5内核初始化接口
        QbSdk.initX5Environment(context, cb);
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
        android.os.Process.killProcess(android.os.Process.myPid());//再此之前可以做些退出等操作
    }
}
