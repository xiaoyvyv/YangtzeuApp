package com.yangtzeu.ui.view;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


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

    SwipeRefreshLayout getRefresh();

    TextView getInternetState();
}
