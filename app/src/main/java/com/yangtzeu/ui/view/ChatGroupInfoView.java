package com.yangtzeu.ui.view;

import android.widget.LinearLayout;
import android.widget.TextView;

import com.lib.chat.adapter.GroupInnerUserAdapter;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import cn.bingoogolapple.bgabanner.BGABanner;

public interface ChatGroupInfoView {
    Toolbar getToolbar();

    BGABanner getBanner();

    RecyclerView getRecyclerView();

    TextView getName();

    TextView getNumber();

    LinearLayout getMineInfoLayout();

    String getTopicId();

    GroupInnerUserAdapter getAdapter();
}
