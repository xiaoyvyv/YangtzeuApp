package com.yangtzeu.presenter;

import android.app.Activity;

import com.yangtzeu.model.ChatGroupInfoModel;
import com.yangtzeu.ui.view.ChatGroupInfoView;

public class ChatGroupInfoPresenter {
    private Activity activity;
    private ChatGroupInfoView view;
    private ChatGroupInfoModel model;

    public ChatGroupInfoPresenter(Activity activity, ChatGroupInfoView view) {
        this.activity = activity;
        this.view = view;
        model = new ChatGroupInfoModel();
    }

    public void loadGroupInfo() {
        model.loadGroupInfo(activity, view);
    }
    public void inviteJoinGroup() {
        model.inviteJoinGroup(activity, view);
    }

}
