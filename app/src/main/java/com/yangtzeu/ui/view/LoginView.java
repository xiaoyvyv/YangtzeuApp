package com.yangtzeu.ui.view;

import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;

public interface LoginView {
    TextInputEditText getNumberView();
    TextInputEditText getPassWordView();
    Button getLoginButton();
    TextView getHistoryView();
}
