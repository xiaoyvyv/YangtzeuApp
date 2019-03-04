package com.yangtzeu.ui.view;


import android.view.View;

import com.lib.notice.NoticeView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import cn.bingoogolapple.bgabanner.BGABanner;

public interface ManyView {
    NoticeView getNoticeView();
    BGABanner getBGABanner();
    RecyclerView getRecyclerView();
    SwipeRefreshLayout getRefresh();

    BGABanner getBGABannerItem();
}
