package com.yangtzeu.ui.activity.base;

import android.Manifest;
import android.os.Bundle;

import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.yangtzeu.R;
import com.yangtzeu.model.LockModel;
import com.yangtzeu.model.imodel.IBaseMode;
import com.yangtzeu.utils.MyUtils;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public abstract class BaseActivity extends AppCompatActivity implements IBaseMode {
    private static final String WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;//需要请求的权限
    private static final String READ_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE;//需要请求的权限
    private static final String READ_PHONE_STATE = Manifest.permission.READ_PHONE_STATE;//需要请求的权限
    private String[] permission = new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE, READ_PHONE_STATE};

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
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
}
