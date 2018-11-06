package com.yangtzeu.ui.view;

import android.app.ProgressDialog;
import android.widget.ImageView;
import android.widget.TextView;

public interface TranslateView {

    TextView getFromText();

    ImageView doPlayView();
    TextView getToText();

    ProgressDialog getDialog();
}
