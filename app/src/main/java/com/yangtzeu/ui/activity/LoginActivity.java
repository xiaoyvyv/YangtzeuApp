package com.yangtzeu.ui.activity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.google.android.material.textfield.TextInputEditText;
import com.yangtzeu.R;
import com.yangtzeu.presenter.LoginPresenter;
import com.yangtzeu.ui.activity.base.BaseActivity;
import com.yangtzeu.ui.view.LoginView;
import com.yangtzeu.url.Url;
import com.yangtzeu.utils.MyUtils;
import com.yangtzeu.utils.PostUtils;

import androidx.annotation.Nullable;

public class LoginActivity extends BaseActivity implements LoginView {
    private Button loginButton;
    private TextInputEditText passwordView;
    private TextInputEditText numberView;
    private TextView belong;
    private TextView history;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }

    @Override
    public void findViews() {
        loginButton = findViewById(R.id.loginButton);
        passwordView = findViewById(R.id.passwordView);
        numberView = findViewById(R.id.numberView);
        belong = findViewById(R.id.belong);
        history = findViewById(R.id.history);
    }

    @Override
    public void setEvents() {
        String crashCause = getIntent().getStringExtra("crashCause");
        String crashMessage = getIntent().getStringExtra("crashMessage");
        if (crashCause != null || crashMessage != null) {
            PostUtils.sendBug(crashCause, crashMessage);
        }
        boolean isCrash = getIntent().getBooleanExtra("isCrash", false);
        if (isCrash) {
            MyUtils.getAlert(LoginActivity.this, "发现了App漏洞，已提交！\n给您带来的不便请谅解！", null).show();
        }

        LoginPresenter presenter = new LoginPresenter(this, this);
        presenter.loadLoginEvent();
        presenter.loadHistory();

        belong.setText(Html.fromHtml(getString(R.string.belong_xy)));
    }

    @Override
    public TextInputEditText getNumberView() {
        return numberView;
    }

    @Override
    public TextInputEditText getPassWordView() {
        return passwordView;
    }

    @Override
    public Button getLoginButton() {
        return loginButton;
    }

    @Override
    public TextView getHistoryView() {
        return history;
    }

    public void onClickLogin(View view) {
        switch (view.getId()) {
            case R.id.yangtzeu_web:
                MyUtils.openUrl(this, Url.Yangtzeu_Base_Url);
                break;
            case R.id.forget_pass:
                ToastUtils.showShort(R.string.deal_forget_pass);
                break;
            case R.id.belong:
                AlertDialog dialog = new AlertDialog.Builder(LoginActivity.this)
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
