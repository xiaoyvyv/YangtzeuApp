package com.yangtzeu.ui.view;

import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.yangtzeu.ui.adapter.ConversationAdapter;

import androidx.recyclerview.widget.RecyclerView;

public interface ChatView {

     SmartRefreshLayout getRefresh();
     RecyclerView getRecyclerView();

    ConversationAdapter getAdapter();
    TextView getConnectTrip();
}
