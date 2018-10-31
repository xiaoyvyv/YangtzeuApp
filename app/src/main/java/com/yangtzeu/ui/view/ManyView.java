package com.yangtzeu.ui.view;


import com.lib.notice.NoticeView;

import androidx.recyclerview.widget.RecyclerView;
import cn.bingoogolapple.bgabanner.BGABanner;

public interface ManyView {
    NoticeView getNoticeView();
    BGABanner getBGABanner();
    RecyclerView getRecyclerView();
}
