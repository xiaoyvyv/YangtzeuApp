package com.yangtzeu.ui.view;

import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

public interface SplashView {
    ImageView getAdView();

    TextView getAdTitle();

    FrameLayout getAdContainer();
}
