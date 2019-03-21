package com.yangtzeu.ui.view;

import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;

import cn.bingoogolapple.bgabanner.BGABanner;

public interface LoginView {
    TextInputEditText getNumberView();
    TextInputEditText getPassWordView();
    Button getLoginButton();
    TextView getHistoryView();

    BGABanner getBanner();
    Button getOfflineLoginButton();
}
