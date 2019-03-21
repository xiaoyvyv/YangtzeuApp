package com.yangtzeu.ui.view;


import android.widget.LinearLayout;

import com.lib.notice.NoticeView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import cn.bingoogolapple.bgabanner.BGABanner;

public interface ManyView {
    NoticeView getNoticeView();
    BGABanner getBGABanner();
    RecyclerView getRecyclerView();
    SwipeRefreshLayout getRefresh();

    LinearLayout getItemLayout();
}
