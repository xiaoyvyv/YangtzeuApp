package com.yangtzeu.app;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import com.blankj.utilcode.util.CrashUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.Utils;
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
import com.yangtzeu.ui.activity.LoginActivity;
import com.yangtzeu.utils.ALiOssUtils;


import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

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
        //初始化OkHttp
        OkHttp.initOkHttp(this);
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
}
