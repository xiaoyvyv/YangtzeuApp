package com.yangtzeu.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

import com.yangtzeu.R;
import com.yangtzeu.ui.activity.base.BaseActivity;
import com.yangtzeu.utils.MyUtils;

public class ToolActivity extends BaseActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tool);
        MyUtils.setToolbarBackToHome(this,toolbar);

    }

    @Override
    public void findViews() {
        toolbar = findViewById(R.id.toolbar);
    }

    @Override
    public void setEvents() {

    }
}
