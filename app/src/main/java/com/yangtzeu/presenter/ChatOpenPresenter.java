package com.yangtzeu.presenter;

import android.app.Activity;

import com.yangtzeu.model.ChatOpenModel;
import com.yangtzeu.ui.view.ChatOpenView;

public class ChatOpenPresenter {
    private Activity activity;
    private ChatOpenView view;
    private ChatOpenModel mode;

    public ChatOpenPresenter(Activity activity, ChatOpenView view) {
        this.activity = activity;
        this.view = view;
        mode = new ChatOpenModel();
    }

    public void loadHistoryMessage() {
        mode.loadHistoryMessage(activity, view);
    }

    public void sendMessage() {
        mode.sendMessage(activity, view);
    }

    public void setMessListener() {
        mode.setMessListener(activity, view);
    }
}
