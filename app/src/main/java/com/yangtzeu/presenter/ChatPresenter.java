package com.yangtzeu.presenter;

import android.app.Activity;

import com.yangtzeu.model.ChatModel;
import com.yangtzeu.ui.view.ChatView;

public class ChatPresenter {
    private Activity activity;
    private ChatView view;
    private ChatModel model;

    public ChatPresenter(Activity activity, ChatView view) {
        this.activity = activity;
        this.view = view;
        model = new ChatModel();
    }

    public void createGroup() {
        model.createGroup(activity, view);
    }
    public void loadYangtzeuGroupInfo() {
        model.loadYangtzeuGroupInfo(activity, view);
    }
}
