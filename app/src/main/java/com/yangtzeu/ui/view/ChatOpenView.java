package com.yangtzeu.ui.view;

import com.google.android.material.textfield.TextInputEditText;
import com.lib.chat.adapter.ChatAdapter;

import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public  interface ChatOpenView {
    SwipeRefreshLayout getRefresh();

    ChatAdapter getAdapter();

    String getContactType();

    String getContactId();

    String getCount();

    String getEndTime();

    void setEndTime(long end_time);


    void scrollBottom();

    TextInputEditText getSendText();

    String getBizType();

    String getBackGround();

}
