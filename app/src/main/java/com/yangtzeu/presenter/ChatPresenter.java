package com.yangtzeu.presenter;

import android.app.Activity;

import com.yangtzeu.model.ChatModel;
import com.yangtzeu.ui.view.ChatView;

public class ChatPresenter {
    private Activity activity;
    private ChatView view;
    private ChatModel mode;

    public ChatPresenter(Activity activity, ChatView view) {
        this.activity = activity;
        this.view = view;
        mode = new ChatModel();
    }


    public void addMessageReceiver() {
        mode.addMessageReceiver(activity, view);
    }

    public void addGeneralReceiver() {
        mode.addGeneralReceiver(activity, view);
    }

    public void removeMessageReceiver() {
        mode.removeMessageReceiver(activity, view);
    }

    public void removeGeneralReceiver() {
        mode.removeGeneralReceiver(activity, view);
    }


    public void getAllLocalConversations() {
        mode.getAllLocalConversations(activity, view);
    }

    public void loadAllGroupInfo() {
        mode.loadAllGroupInfo(activity, view);
    }
}
