package com.yangtzeu.ui.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.lib.subutil.ClipboardUtils;
import com.yangtzeu.R;
import com.yangtzeu.presenter.TranslatePresenter;
import com.yangtzeu.ui.activity.base.BaseActivity;
import com.yangtzeu.ui.view.TranslateView;
import com.yangtzeu.utils.MediaPlayUtil;
import com.yangtzeu.utils.MyUtils;

import androidx.appcompat.widget.Toolbar;


public class TranslateActivity extends BaseActivity implements TranslateView {
    private Toolbar toolbar;
    private TextView fromText;
    private TextView toText;
    private FloatingActionButton do_translate;
    private TranslatePresenter president;
    private ProgressDialog dialog;
    private ImageView play;
    private ImageView copy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translate);
        init();
        MyUtils.setToolbarBackToHome(this, toolbar);
    }

    @Override
    public void findViews() {
        toolbar = findViewById(R.id.toolbar);
        do_translate = findViewById(R.id.do_translate);
        fromText = findViewById(R.id.fromText);
        toText = findViewById(R.id.toText);
        play = findViewById(R.id.play);
        copy = findViewById(R.id.copy);

    }

    @Override
    public void setEvents() {
        dialog = MyUtils.getProgressDialog(this, getString(R.string.loading));

        president = new TranslatePresenter(this, this);
        do_translate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                president.translate();
            }
        });


        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showShort(R.string.please_translate);
            }
        });
        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = toText.getText().toString().trim();
                if (StringUtils.isEmpty(text)) {
                    ToastUtils.showShort(R.string.please_translate);
                } else {
                    ToastUtils.showShort(R.string.copy_right);
                    ClipboardUtils.copyText(text);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(R.string.clean).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                fromText.setText(null);
                toText.setText(null);
                return false;
            }
        }).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onDestroy() {
        MediaPlayUtil.getInstance().stop();
        super.onDestroy();
    }
    @Override
    public TextView getFromText() {
        return fromText;
    }

    @Override
    public ImageView doPlayView() {
        return play;
    }

    @Override
    public TextView getToText() {
        return toText;
    }

    @Override
    public ProgressDialog getDialog() {
        return dialog;
    }
}
