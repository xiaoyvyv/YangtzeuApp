package com.yangtzeu.presenter;

import android.app.Activity;

import com.mob.imsdk.model.IMMessage;
import com.yangtzeu.model.ChatDetailsModel;
import com.yangtzeu.ui.view.ChatDetailsView;

public class ChatDetailsPresenter {
    private Activity activity;
    private ChatDetailsView view;
    private ChatDetailsModel mode;

    public ChatDetailsPresenter(Activity activity, ChatDetailsView view) {
        this.activity = activity;
        this.view = view;
        mode = new ChatDetailsModel();
    }


    public void addMessageReceiver() {
        mode.addMessageReceiver(activity, view);
    }

    public void removeMessageReceiver() {
        mode.removeMessageReceiver(activity, view);
    }

    public void sendMessage() {
        mode.sendMessage(activity, view);
    }

    public void setUserInfo() {
        mode.setUserInfo(activity, view);
    }

    public void addMsgToChatView(IMMessage imMessage) {
        mode.addMsgToChatView(activity, view, imMessage);
    }

    public void setChatViewParam() {
        mode.setChatViewParam(activity, view);
    }
}
