package com.yangtzeu.model.imodel;

import android.app.Activity;

import com.lib.chat.bean.MessagesBean;
import com.yangtzeu.ui.view.ChatOpenView;

import java.util.List;

public interface IChatOpenModel {
    void loadHistoryMessage(Activity activity, ChatOpenView view);

    void showMessage(Activity activity, ChatOpenView view, List<MessagesBean> messages, boolean isTop);

    void sendMessage(Activity activity, ChatOpenView view);

    void setMessListener(Activity activity, ChatOpenView view);
}
