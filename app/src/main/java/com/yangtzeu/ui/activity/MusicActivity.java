package com.yangtzeu.ui.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.android.material.textfield.TextInputEditText;
import com.lib.subutil.ClipboardUtils;
import com.yangtzeu.R;
import com.yangtzeu.http.OkHttp;
import com.yangtzeu.http.OnResultStringListener;
import com.yangtzeu.listener.OnResultListener;
import com.yangtzeu.ui.activity.base.BaseActivity;
import com.yangtzeu.url.Url;
import com.yangtzeu.utils.MediaPlayUtil;
import com.yangtzeu.utils.MyUtils;
import com.yangtzeu.utils.YangtzeuUtils;

import java.util.Objects;

import androidx.appcompat.widget.Toolbar;

public class MusicActivity extends BaseActivity {

    private Toolbar toolbar;
    private TextInputEditText music_name;
    private TextView link;
    private String path;
    private LinearLayout container;
    private String name = "有点甜";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);
        init();
        MyUtils.setToolbarBackToHome(this,toolbar);
    }

    @Override
    public void findViews() {
        toolbar = findViewById(R.id.toolbar);
        music_name = findViewById(R.id.music_name);
        link = findViewById(R.id.link);
        container = findViewById(R.id.container);

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void setEvents() {
        container.setVisibility(View.GONE);
    }


    public void click(View view) {
        switch (view.getId()) {
            case R.id.search:
                 name = Objects.requireNonNull(music_name.getText()).toString().trim();
                if (ObjectUtils.isNotEmpty(name)) {
                    ToastUtils.showShort(R.string.loading);
                    getMusicLink(name, new OnResultListener<String>() {
                        @Override
                        public void onResult(String s) {
                            path = s;
                            link.setText(s);
                            link.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    ClipboardUtils.copyText(path);
                                    ToastUtils.showShort(R.string.copy_right);
                                }
                            });
                            container.setVisibility(View.VISIBLE);
                            ToastUtils.showShort(R.string.load_success);
                        }
                    });
                } else {
                    music_name.setError(getString(R.string.input_music));
                    music_name.requestFocus();
                }
                break;
            case R.id.play:
                if (!path.isEmpty())
                    MediaPlayUtil.getInstance().play(path);
                break;
            case R.id.choose:
                if (!path.isEmpty()) {
                    MusicActivity.this.onBackPressed();
                } else {
                    ToastUtils.showShort(R.string.loading);
                }
                break;
        }
    }

    public static void getMusicLink(String name, final OnResultListener<String> listener) {
        OkHttp.do_Get("http://api.guaqb.cn/api.php?yy=" + name, new OnResultStringListener() {
            @Override
            public void onResponse(String response) {
                LogUtils.e(response);
                if (URLUtil.isNetworkUrl(response)) {
                    if (listener != null) {
                        listener.onResult(response);
                    }
                } else {
                    ToastUtils.showShort(R.string.no_music_data);
                }
            }
            @Override
            public void onFailure(String error) {
                ToastUtils.showShort(R.string.load_error);
            }
        });
    }

    @Override
    protected void onDestroy() {
        MediaPlayUtil.getInstance().stop();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("music_name", name);
        setResult(666,intent);
        super.onBackPressed();
    }
}
