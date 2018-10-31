package com.yangtzeu.ui.view;

import android.widget.Button;
import android.widget.EditText;

import com.lib.mob.chat.ChatUser;
import com.yangtzeu.ui.adapter.ChatViewAdapter;

import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;

public interface ChatDetailsView {
    String getFromId();
    ChatUser[] getChatUser();
    ChatViewAdapter getChatAdapter();
    RecyclerView getChatView();
    EditText getInputView();
    Button getSendButton();

    NestedScrollView getNestedScrollView();
}
