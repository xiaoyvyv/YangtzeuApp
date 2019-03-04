package com.yangtzeu.presenter;

import android.app.Activity;

import com.yangtzeu.model.ChatPart1Model;
import com.yangtzeu.model.ChatPart2Model;
import com.yangtzeu.ui.view.ChatPartView1;
import com.yangtzeu.ui.view.ChatPartView2;

public class ChatPart2Presenter {
    private Activity activity;
    private ChatPartView2 view;
    private ChatPart2Model model;

    public ChatPart2Presenter(Activity activity, ChatPartView2 view) {
        this.activity = activity;
        this.view = view;
        model = new ChatPart2Model();
    }

    public void loadContacts() {
        model.loadContacts(activity, view);
    }

    public void addAdmin() {
        model.addAdmin(activity, view);
    }
}
