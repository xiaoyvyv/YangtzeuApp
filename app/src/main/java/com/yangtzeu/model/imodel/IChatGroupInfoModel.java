package com.yangtzeu.model.imodel;

import android.app.Activity;

import com.lib.chat.bean.CreateGroupBean;
import com.yangtzeu.ui.view.ChatGroupInfoView;

public interface IChatGroupInfoModel {

    void loadGroupInfo(Activity activity, ChatGroupInfoView view);

    void loadMembersInfo(Activity activity, ChatGroupInfoView view,  CreateGroupBean.DataBean dataBean);

    void loadExtraInfo(Activity activity, ChatGroupInfoView view, String extra);

    void updateGroupInfo(Activity activity, ChatGroupInfoView chatGroupInfoView,final CreateGroupBean.DataBean topicInfo,  boolean isOnlyBulletin);

    void inviteJoinGroup(Activity activity, ChatGroupInfoView chatView);
}
