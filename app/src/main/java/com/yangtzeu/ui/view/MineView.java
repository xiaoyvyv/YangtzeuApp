package com.yangtzeu.ui.view;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


public interface MineView {
    ImageView getHeader();

    TextView getClassView();

    TextView getNameView();

    Toolbar getToolbar();

    TextView getMessageView();


    CardView getStatusView();

    TextView getStatusTextView();

    LinearLayout getMessageLayout();
    LinearLayout getPhysicalLayout();
    LinearLayout getCardLayout();

    TextView getMessageImage();

    SwipeRefreshLayout getRefresh();

    TextView getInternetState();

}
