package com.yangtzeu.model;

import android.app.Activity;
import android.text.Editable;
import android.widget.ScrollView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.lib.mob.chat.ChatUser;
import com.lib.mob.chat.ChatViewUtils;
import com.lib.mob.im.IMManager;
import com.mob.imsdk.MobIM;
import com.mob.imsdk.MobIMCallback;
import com.mob.imsdk.MobIMMessageReceiver;
import com.mob.imsdk.model.IMMessage;
import com.mob.imsdk.model.IMUser;
import com.yangtzeu.R;
import com.yangtzeu.entity.ChatBean;
import com.yangtzeu.listener.OnResultListener;
import com.yangtzeu.listener.SimpleTextChangeListener;
import com.yangtzeu.model.imodel.IChatDetailsModel;
import com.yangtzeu.ui.view.ChatDetailsView;
import com.yangtzeu.utils.MyUtils;

import java.util.List;
import java.util.Objects;


public class ChatDetailsModel implements IChatDetailsModel {
    private MobIMMessageReceiver mobIMMessageReceiver;

    @Override
    public void addMessageReceiver(final Activity activity, final ChatDetailsView view) {
        if (mobIMMessageReceiver == null) {
            mobIMMessageReceiver = new MobIMMessageReceiver() {
                @Override
                public void onMessageReceived(List<IMMessage> list) {
                    MyUtils.mVibrator(activity, 500);
                    for (int i = 0; i < list.size(); i++) {
                        addMsgToChatView(activity, view, list.get(i));
                        MobIM.getChatManager().markMessageAsRead(list.get(i).getId());
                    }
                }

                @Override
                public void onMsgWithDraw(String s, String s1) {

                }
            };
        }
        MobIM.addMessageReceiver(mobIMMessageReceiver);

    }

    @Override
    public void removeMessageReceiver(Activity activity, ChatDetailsView view) {
        if (mobIMMessageReceiver != null) {
            MobIM.removeMessageReceiver(mobIMMessageReceiver);
        }
    }

    @Override
    public void sendMessage(final Activity activity, final ChatDetailsView view) {
        String string = Objects.requireNonNull(view.getInputView().getText()).toString();
        if (ObjectUtils.isEmpty(string)) {
            ToastUtils.showShort(R.string.input_message);
            return;
        }
        view.getInputView().setText("");

        //向服务器发送消息
        IMMessage message = MobIM.getChatManager().createTextMessage(view.getFromId(), string, IMMessage.TYPE_USER);
        MobIM.getChatManager().sendMessage(message, new MobIMCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                ToastUtils.showShort(R.string.send_chat_success);
            }

            @Override
            public void onError(int i, String s) {
                ToastUtils.showShort(s);
                LogUtils.e("消息发送失败", i, s);
            }
        });
        addMsgToChatView(activity, view, message);
    }

    @Override
    public void setUserInfo(final Activity activity, final ChatDetailsView view) {
        MobIM.getUserManager().getUserInfo(view.getFromId(), new MobIMCallback<IMUser>() {
            @Override
            public void onSuccess(IMUser imUser) {
                //设置对方的 User
                ChatViewUtils.IMUser2ChatUser(activity, imUser, new OnResultListener<ChatUser>() {
                    @Override
                    public void onResult(ChatUser s) {
                        view.getChatUser()[0] = s;
                    }
                });
                //设置我的 User
                IMUser myUser = IMManager.getUser();
                ChatViewUtils.IMUser2ChatUser(activity, myUser, new OnResultListener<ChatUser>() {
                    @Override
                    public void onResult(ChatUser s) {
                        view.getChatUser()[1] = s;
                    }
                });
            }

            @Override
            public void onError(int i, String s) {
                LogUtils.e("查询用户信息失败", i, s);
            }
        });
    }

    @Override
    public void setChatViewParam(Activity activity, final ChatDetailsView view) {
        view.getChatView().setAdapter(view.getChatAdapter());
        view.getChatView().setNestedScrollingEnabled(false);
        view.getSendButton().setEnabled(false);
        view.getInputView().addTextChangedListener(new SimpleTextChangeListener(new OnResultListener<Editable>() {
            @Override
            public void onResult(Editable s) {
                String string = s.toString();
                if (StringUtils.isEmpty(string)) {
                    view.getSendButton().setEnabled(false);
                } else {
                    view.getSendButton().setEnabled(true);
                }
            }
        }));
    }

    @Override
    public void addMsgToChatView(final Activity activity, final ChatDetailsView view, final IMMessage imMessage) {
        ChatViewUtils.IMUser2ChatUser(activity, imMessage.getFromUserInfo(), new OnResultListener<ChatUser>() {
            @Override
            public void onResult(ChatUser s) {
                ChatBean chatBean = new ChatBean.Builder()
                        .setUser(s)
                        .setRight(StringUtils.equals(s.getId(), IMManager.getUser().getId()))
                        .setState(ChatBean.NOT_READ)
                        .setText(imMessage.getBody())
                        .setTime(TimeUtils.millis2String(imMessage.getCreateTime()))
                        .setType(ChatBean.Type.TEXT)
                        .build();
                view.getChatAdapter().sendMessage(chatBean);
                view.getNestedScrollView().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        view.getNestedScrollView().fullScroll(ScrollView.FOCUS_DOWN);
                        view.getInputView().requestFocus();
                    }
                }, 100);
            }
        });
    }
}
