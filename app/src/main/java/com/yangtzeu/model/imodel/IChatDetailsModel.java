package com.yangtzeu.model.imodel;

import android.app.Activity;

import com.mob.imsdk.model.IMMessage;
import com.yangtzeu.ui.view.ChatDetailsView;

public interface IChatDetailsModel {
    void addMessageReceiver(Activity activity, ChatDetailsView view);

    void removeMessageReceiver(Activity activity, ChatDetailsView view);


    void sendMessage(Activity activity, ChatDetailsView view);

    void setUserInfo(Activity activity, ChatDetailsView view);

    void addMsgToChatView(Activity activity, ChatDetailsView view, IMMessage imMessage);

    void setChatViewParam(Activity activity, ChatDetailsView view);
}
