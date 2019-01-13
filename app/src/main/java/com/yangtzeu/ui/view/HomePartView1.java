package com.yangtzeu.ui.view;

import android.view.View;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

public interface HomePartView1 {
    View getRootView();
    TextView getNoticeTitle();
    TextView getNoticeText();
    CardView getNoticeLayout();
}
