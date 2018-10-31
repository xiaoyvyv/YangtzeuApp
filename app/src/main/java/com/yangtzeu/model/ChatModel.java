package com.yangtzeu.model;

import android.app.Activity;
import android.view.View;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.mob.imsdk.MobIM;
import com.mob.imsdk.MobIMCallback;
import com.mob.imsdk.MobIMMessageReceiver;
import com.mob.imsdk.MobIMReceiver;
import com.mob.imsdk.model.IMConversation;
import com.mob.imsdk.model.IMMessage;
import com.yangtzeu.R;
import com.yangtzeu.model.imodel.IChatModel;
import com.yangtzeu.ui.view.ChatView;
import com.yangtzeu.utils.MyUtils;

import java.util.List;


public class ChatModel implements IChatModel {
    private MobIMMessageReceiver messageReceiver;
    private MobIMReceiver mobIMReceiver;

    @Override
    public void addMessageReceiver(final Activity activity, final ChatView view) {
        messageReceiver = new MobIMMessageReceiver() {
            @Override
            public void onMessageReceived(List<IMMessage> list) {
                MyUtils.mVibrator(activity, 1000);
                getAllLocalConversations(activity, view);
            }

            @Override
            public void onMsgWithDraw(String s, String s1) {

            }
        };
        MobIM.addMessageReceiver(messageReceiver);
    }

    @Override
    public void addGeneralReceiver(final Activity activity, final ChatView view) {
        mobIMReceiver = new MobIMReceiver() {
            @Override
            public void onConnected() {
                view.getConnectTrip().setVisibility(View.GONE);
                view.getConnectTrip().setText(R.string.connect_mob_success);
            }

            @Override
            public void onConnecting() {
                view.getConnectTrip().setVisibility(View.VISIBLE);
                view.getConnectTrip().setText(R.string.connect_mob);
            }

            @Override
            public void onDisconnected(int i) {
                view.getConnectTrip().setVisibility(View.VISIBLE);
                view.getConnectTrip().setText(R.string.connect_mob_error);
            }
        };
        MobIM.addGeneralReceiver(mobIMReceiver);
    }

    @Override
    public void removeMessageReceiver(Activity activity, ChatView view) {
        if (messageReceiver != null) {
            MobIM.removeMessageReceiver(messageReceiver);
        }
    }


    @Override
    public void removeGeneralReceiver(Activity activity, ChatView view) {
        if (mobIMReceiver != null) {
            MobIM.removeGeneralReceiver(mobIMReceiver);
        }
    }

    @Override
    public void getAllLocalConversations(Activity activity, final ChatView view) {
        MobIM.getChatManager().getAllLocalConversations(new MobIMCallback<List<IMConversation>>() {
            @Override
            public void onSuccess(List<IMConversation> imConversations) {
                if (imConversations.size() == 0) {
                    ToastUtils.showShort(R.string.no_chat_message);
                } else {
                    view.getAdapter().setData(imConversations);
                    view.getAdapter().notifyItemRangeChanged(0, view.getAdapter().getItemCount());
                }
            }

            @Override
            public void onError(int code, String message) {
                ToastUtils.showShort(message);
            }
        });
    }

}
