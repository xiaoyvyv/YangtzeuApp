package com.yangtzeu.ui.view;

import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lib.x5web.WebViewProgressBar;

public interface LoveAddView {
    Button getSend();

    LinearLayout getAdd_image_trip();

    String getImagePath();

    WebViewProgressBar getProgressView();

    String getMusic();

    TextView getDes();

    TextView getNameTa();

    boolean isHide();

    TextView getQQTa();
}
