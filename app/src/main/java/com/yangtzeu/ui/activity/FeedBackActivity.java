package com.yangtzeu.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.blankj.utilcode.util.SPUtils;
import com.google.android.material.textfield.TextInputEditText;
import com.yangtzeu.R;
import com.yangtzeu.presenter.FeedBackPresenter;
import com.yangtzeu.ui.activity.base.BaseActivity;
import com.yangtzeu.ui.view.FeedBackView;
import com.yangtzeu.utils.MyUtils;

import androidx.appcompat.widget.Toolbar;


public class FeedBackActivity extends BaseActivity implements FeedBackView {

    private Toolbar toolbar;
    private TextInputEditText suggestion;
    private TextInputEditText qq;
    private Button send;
    private FeedBackPresenter president;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        init();
        MyUtils.setToolbarBackToHome(this, toolbar);
    }

    @Override
    public void findViews() {
        toolbar = findViewById(R.id.toolbar);
        suggestion = findViewById(R.id.suggestion);
        qq = findViewById(R.id.qq);
        send = findViewById(R.id.send);
    }

    @Override
    public void setEvents() {
        String my_qq = SPUtils.getInstance("user_info").getString("qq", "2440888027");
        qq.setText(my_qq);

        president = new FeedBackPresenter(this, this);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                president.sendSuggestion();
            }
        });

    }

    @Override
    public TextInputEditText getSuggestionText() {
        return suggestion;
    }

    @Override
    public TextInputEditText getQQ() {
        return qq;
    }
}
