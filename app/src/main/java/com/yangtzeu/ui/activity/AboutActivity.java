package com.yangtzeu.ui.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.yangtzeu.R;
import com.yangtzeu.ui.activity.base.BaseActivity;
import com.yangtzeu.url.Url;
import com.yangtzeu.utils.MyUtils;
import com.yangtzeu.utils.YangtzeuUtils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class AboutActivity extends BaseActivity {

    private Toolbar toolbar;
    private TextView app_version;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        init();
        MyUtils.setToolbarBackToHome(this,toolbar);
    }

    @Override
    public void findViews() {
        toolbar = findViewById(R.id.toolbar);
        app_version = findViewById(R.id.app_version);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void setEvents() {
        String appVersionName = MyUtils.getAPPVersionName(this);
        app_version.setText("V" + appVersionName);
    }

    public void about_click(View view) {
        switch (view.getId()) {
            case R.id.launcher:
                MyUtils.openBrowser(this, Url.AppDownUrl);
                break;
            case R.id.checkUpdate:
                YangtzeuUtils.checkAppVersion(this);
                break;
            case R.id.helpLayout:
                MyUtils.openBrowser(this, Url.AppDownUrl);
                break;
            case R.id.feedBackLayout:
                MyUtils.startActivity(FeedBackActivity.class);
                break;
            case R.id.yangtzeu_home:
                MyUtils.openBrowser(this, Url.Yangtzeu_Url);
                break;
            case R.id.app_rule:
                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setTitle(R.string.app_rule)
                        .setMessage(getString(R.string.app_rule_text1) +
                                getString(R.string.app_rule_text2))
                        .setPositiveButton(R.string.know, null)
                        .create();
                dialog.show();
                dialog.setCanceledOnTouchOutside(false);
                dialog.setCancelable(false);
                break;
        }
    }
}
