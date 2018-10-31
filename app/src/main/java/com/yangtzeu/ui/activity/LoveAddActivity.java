package com.yangtzeu.ui.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.lib.x5web.WebViewProgressBar;
import com.yangtzeu.R;
import com.yangtzeu.presenter.LoveAddPresenter;
import com.yangtzeu.ui.activity.base.BaseActivity;
import com.yangtzeu.ui.view.LoveAddView;
import com.yangtzeu.utils.MyUtils;
import com.yangtzeu.utils.UriPathUtils;

import java.util.Objects;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

public class LoveAddActivity extends BaseActivity implements LoveAddView {
    private Toolbar toolbar;
    private LinearLayout add_image_trip;
    private ImageView image;
    private TextView des;
    private TextView name_ta;
    private TextView music;
    private TextView qq_ta;
    private Button send;
    private LoveAddPresenter president;
    private String imagePath;
    private String music_name;
    private WebViewProgressBar progressBar;
    private TextView isHideView;
    private boolean isHide = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_love_add);
        init();
        MyUtils.setToolbarBackToHome(this, toolbar);
    }

    @Override
    public void findViews() {
        toolbar = findViewById(R.id.toolbar);
        name_ta = findViewById(R.id.name_ta);
        des = findViewById(R.id.des);
        image = findViewById(R.id.image);
        add_image_trip = findViewById(R.id.add_image_trip);
        qq_ta = findViewById(R.id.qq_ta);
        music = findViewById(R.id.music);
        send = findViewById(R.id.send);
        progressBar = findViewById(R.id.progressBar);
        isHideView = findViewById(R.id.isHide);

    }
    @Override
    public void setEvents() {
        president = new LoveAddPresenter(this, this);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                president.sendLove();
            }
        });

        add_image_trip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pickIntent = new Intent(Intent.ACTION_PICK);
                pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(pickIntent, 999);
            }
        });

        music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoveAddActivity.this, MusicActivity.class);
                startActivityForResult(intent, 666);
                MyUtils.enterAnimation(ActivityUtils.getTopActivity());
            }
        });
        isHideView.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                if (isHide) {
                    isHide = false;
                    isHideView.setText("公开表白");
                } else {
                    isHide = true;
                    isHideView.setText("匿名表白  （trip：喜欢就要告诉他）");
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == 999) {
            imagePath = UriPathUtils.getPath(this, Objects.requireNonNull(data).getData());
            Glide.with(this).load(imagePath).into(image);
            add_image_trip.removeAllViews();
        } else if (resultCode == 666) {
            if (data != null) {
                music_name = data.getStringExtra("music_name");
                music.setText(music_name);
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            ToastUtils.showShort(R.string.clear_set);
        } else ToastUtils.showShort(R.string.load_file_error);
    }

    @Override
    public LinearLayout getAdd_image_trip() {
        return add_image_trip;
    }

    @Override
    public Button getSend() {
        return send;
    }


    @Override
    public String getImagePath() {
        return imagePath;
    }

    @Override
    public WebViewProgressBar getProgressView() {
        return progressBar;
    }

    @Override
    public TextView getDes() {
        return des;
    }

    @Override
    public TextView getNameTa() {
        return name_ta;
    }

    @Override
    public boolean isHide() {
        return isHide;
    }

    @Override
    public String getMusic() {
        return music_name;
    }

    @Override
    public TextView getQQTa() {
        return qq_ta;
    }
}
