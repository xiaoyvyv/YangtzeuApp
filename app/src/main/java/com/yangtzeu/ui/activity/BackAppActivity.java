package com.yangtzeu.ui.activity;


import android.os.Bundle;

import com.yangtzeu.R;
import com.yangtzeu.ui.activity.base.BaseActivity;

public class BackAppActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Translucent);
        setContentView(R.layout.activity_back_app);
        init();
    }

    @Override
    public void findViews() {

    }


    @Override
    public void setEvents() {

    }

    @Override
    public void onBackPressed() {

    }
}
