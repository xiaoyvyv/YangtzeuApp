package com.yangtzeu.model.imodel;

import android.app.Activity;

import com.yangtzeu.ui.view.ChatView;

public interface IChatModel {
    void addMessageReceiver(Activity activity, ChatView view);

    void addGeneralReceiver(Activity activity, ChatView view);

    void removeMessageReceiver(Activity activity, ChatView view);

    void removeGeneralReceiver(Activity activity, ChatView view);

    void getAllLocalConversations(Activity activity, ChatView view);

    void loadAllGroupInfo(Activity activity, ChatView view);
}
