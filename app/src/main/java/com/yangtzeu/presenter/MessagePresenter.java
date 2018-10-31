package com.yangtzeu.presenter;

import android.app.Activity;

import com.yangtzeu.model.MessageModel;
import com.yangtzeu.ui.view.MessageView;

public class MessagePresenter {
    private Activity activity;
    private MessageView view;
    private MessageModel mode;

    public MessagePresenter(Activity activity, MessageView view) {
        this.activity = activity;
        this.view = view;
        mode = new MessageModel();
    }

    public void loadMessageData() {
        mode.loadMessageData(activity, view);
    }
}
