package com.yangtzeu.ui.view;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import androidx.appcompat.widget.Toolbar;


public interface MineView {
    ImageView getHeader();

    TextView getClassView();

    TextView getNameView();

    Toolbar getToolbar();

    TextView getUserCetView();

    TextView getEmailView();

    TextView getMessageView();

    LinearLayout getMessageLayout();
    LinearLayout getPhysicalLayout();
    LinearLayout getCardLayout();

    TextView getMessageImage();

    SmartRefreshLayout getRefresh();

    TextView getInternetState();
}
