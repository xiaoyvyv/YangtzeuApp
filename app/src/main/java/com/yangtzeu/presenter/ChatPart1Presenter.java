package com.yangtzeu.presenter;

import android.app.Activity;

import com.yangtzeu.model.ChatPart1Model;
import com.yangtzeu.ui.view.ChatPartView1;

public class ChatPart1Presenter {
    private Activity activity;
    private ChatPartView1 view;
    private ChatPart1Model model;

    public ChatPart1Presenter(Activity activity, ChatPartView1 view) {
        this.activity = activity;
        this.view = view;
        model = new ChatPart1Model();
    }

    public void setMessageListener() {
        model.setMessageListener(activity, view);
    }

    public void loadMessageHistory() {
        model.loadContactHistory(activity, view);
    }
}
