package com.yangtzeu.ui.activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.blankj.utilcode.util.SPUtils;
import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import com.yangtzeu.R;
import com.yangtzeu.presenter.ChangePassPresenter;
import com.yangtzeu.ui.activity.base.BaseActivity;
import com.yangtzeu.ui.view.ChangePassView;
import com.yangtzeu.url.Url;
import com.yangtzeu.utils.MyUtils;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

public class ChangePassActivity extends BaseActivity implements ChangePassView {
    private Toolbar toolbar;
    private TextInputEditText ACC;
    private TextInputEditText PassOld;
    private TextInputEditText PassNew;
    private TextInputEditText PassDone;
    private Button ChangeBtn;
    private ChangePassPresenter presenter;
    private ImageView default_bg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass);
        init();
        MyUtils.setToolbarBackToHome(this, toolbar);
    }

    @Override
    public void findViews() {
        toolbar = findViewById(R.id.toolbar);
        ACC =  findViewById(R.id.txt);
        PassOld = findViewById(R.id.txt1);
        PassNew =  findViewById(R.id.txt2);
        PassDone =  findViewById(R.id.txt3);
        ChangeBtn =  findViewById(R.id.ChangeBtn);
        default_bg = findViewById(R.id.default_bg);
    }

    @Override
    public void setEvents() {
        Glide.with(this).load(Url.Yangtzeu_Table_Background).into(default_bg);

        presenter = new ChangePassPresenter(this, this);
        String number = SPUtils.getInstance("user_info").getString("number");
        ACC.setText(number);

        String password = SPUtils.getInstance("user_info").getString("password");
        PassOld.setText(password);

        ChangeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.changePassEvent();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.changepass_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.web_change_pass:
                new AlertDialog.Builder(ChangePassActivity.this)
                        .setTitle(R.string.trip)
                        .setMessage("密码必须同时包含【大写字母】、【小写字母】、【数字】才能修改成功！")
                        .setPositiveButton(R.string.know, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                MyUtils.openUrl(ChangePassActivity.this, Url.Yangtzeu_Change_Password);
                            }
                        }).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public Toolbar getToolbar() {
        return toolbar;
    }
    @Override
    public TextInputEditText ACC() {
        return ACC;
    }

    @Override
    public TextInputEditText PassOld() {
        return PassOld;
    }

    @Override
    public TextInputEditText PassNew() {
        return PassNew;
    }

    @Override
    public TextInputEditText PassDone() {
        return PassDone;
    }
}
