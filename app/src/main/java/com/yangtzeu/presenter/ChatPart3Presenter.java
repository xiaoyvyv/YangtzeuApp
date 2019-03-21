package com.yangtzeu.presenter;

import android.app.Activity;

import com.yangtzeu.model.ChatPart3Model;
import com.yangtzeu.ui.view.ChatPartView3;

public class ChatPart3Presenter {
    private Activity activity;
    private ChatPartView3 view;
    private ChatPart3Model model;

    public ChatPart3Presenter(Activity activity, ChatPartView3 view) {
        this.activity = activity;
        this.view = view;
        model = new ChatPart3Model();
    }

    public void loadContacts() {
        model.loadContacts(activity, view);
    }

    public void addAdmin() {
        model.addAdmin(activity, view);
    }
}
