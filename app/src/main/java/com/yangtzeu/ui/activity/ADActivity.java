package com.yangtzeu.ui.activity;

import android.os.Bundle;
import android.view.View;

import com.blankj.utilcode.util.ToastUtils;
import com.lib.subutil.ClipboardUtils;
import com.yangtzeu.R;
import com.yangtzeu.ui.activity.base.BaseActivity;
import com.yangtzeu.utils.MyUtils;

import androidx.appcompat.widget.Toolbar;

public class ADActivity extends BaseActivity {
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad);
        init();
        MyUtils.setToolbarBackToHome(this,toolbar);
    }

    @Override
    public void findViews() {
        toolbar = findViewById(R.id.toolbar);
    }

    @Override
    public void setEvents() {
        ToastUtils.showShort("我是小玉：合作愉快^_^");
    }

    public void chatQQ(View view) {
        ClipboardUtils.copyText("1223414335");
        ToastUtils.showShort("QQ号码已复制！");
        MyUtils.chatQQ(this,"1223414335");
    }

    public void chatWeChat(View view) {
        ClipboardUtils.copyText("whysbelief");
        ToastUtils.showShort("微信号码已复制！");
        MyUtils.openWeChat();
    }
}
