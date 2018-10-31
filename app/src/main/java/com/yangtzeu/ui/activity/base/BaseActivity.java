package com.yangtzeu.ui.activity.base;

import android.os.Bundle;

import com.blankj.utilcode.util.SPUtils;
import com.yangtzeu.R;
import com.yangtzeu.model.imodel.IBaseMode;
import com.yangtzeu.utils.MyUtils;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public abstract class BaseActivity extends AppCompatActivity implements IBaseMode {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        MyUtils.setMyTheme(this);
        super.onCreate(savedInstanceState);
    }
    public void init() {
        findViews();
        setEvents();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
}