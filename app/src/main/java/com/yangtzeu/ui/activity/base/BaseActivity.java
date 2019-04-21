package com.yangtzeu.ui.activity.base;

import android.Manifest;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.yangtzeu.R;
import com.yangtzeu.listener.OnResultListener;
import com.yangtzeu.model.LockModel;
import com.yangtzeu.model.imodel.IBaseMode;
import com.yangtzeu.utils.GoogleUtils;
import com.yangtzeu.utils.MyUtils;

public abstract class BaseActivity extends AppCompatActivity implements IBaseMode {
    private static final String WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;//需要请求的权限
    private static final String READ_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE;//需要请求的权限
    private static final String READ_PHONE_STATE = Manifest.permission.READ_PHONE_STATE;//需要请求的权限
    private String[] permission = new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE, READ_PHONE_STATE};
    private boolean isCurrentRunningForeground = true;
    private long time = 0;

    public String url;
    public String index_url;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        ScreenUtils.setPortrait(this);
        MyUtils.setMyTheme(this);
        super.onCreate(savedInstanceState);
    }

    public void init() {
        findViews();
        setEvents();

        boolean granted = PermissionUtils.isGranted(permission);
        if (granted) {
            //检查是上次否锁定完成
            new LockModel().checkIsLockFinish(this, null, "ACTION_SCREEN_CHECK");
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!isCurrentRunningForeground) {
            isCurrentRunningForeground = true;
            long speed = TimeUtils.getNowMills() - time;
            if (speed > 30000) {
                //处理跳转到广告页逻辑
               /* Intent intent = new Intent(this, BackAppActivity.class);
                startActivity(intent);*/

                GoogleUtils.showInterstitialAd(new OnResultListener<Boolean>() {
                    @Override
                    public void onResult(Boolean s) {
                        ToastUtils.showShort(s + "");
                    }
                });
            }
            LogUtils.e(speed + "ms", ">>>>>>>>>>>>>>>>>>>切回前台 activity process");
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        isCurrentRunningForeground = AppUtils.isAppForeground();
        if (!isCurrentRunningForeground) {
            time = TimeUtils.getNowMills();
            LogUtils.e(">>>>>>>>>>>>>>>>>>>切到后台 activity process");

            GoogleUtils.loadInterstitialAd();
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

}
