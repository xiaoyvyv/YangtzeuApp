package com.yangtzeu.model;

import android.app.Activity;

import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.lib.chat.adapter.ChatAdapter;
import com.lib.chat.bean.MessageHistoryBean;
import com.lib.chat.bean.MessagesBean;
import com.lib.chat.bean.PayLoadBean;
import com.lib.chat.common.Constant;
import com.lib.chat.common.UserManager;
import com.lib.chat.listener.receive.OnReceiveGroupMessageListener;
import com.lib.chat.listener.receive.OnReceiveMessageListener;
import com.lib.chat.listener.send.OnSendMessageListener;
import com.lib.subutil.GsonUtils;
import com.lib.yun.Base64;
import com.xiaomi.mimc.MIMCGroupMessage;
import com.xiaomi.mimc.MIMCMessage;
import com.xiaomi.mimc.MIMCServerAck;
import com.yangtzeu.R;
import com.yangtzeu.http.OnResultStringListener;
import com.yangtzeu.model.imodel.IChatOpenModel;
import com.yangtzeu.ui.view.ChatOpenView;
import com.yangtzeu.utils.MyUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ChatOpenModel implements IChatOpenModel {
    private UserManager userManager = UserManager.getInstance();

    @Override
    public void setMessListener(final Activity activity, final ChatOpenView view) {
        //单聊
        if (StringUtils.equals(view.getContactType(), Constant.USER_TYPE_USER)) {
            userManager.setMessageListener(new OnReceiveMessageListener() {
                @Override
                public void onMessageSuccess(MIMCMessage mimcMessage) {
                    MessagesBean messagesBean = userManager.pToMessagesBean(mimcMessage);
                    List<MessagesBean> messages = new ArrayList<>();
                    messages.add(messagesBean);

                    showMessage(activity, view, messages, false);
                }

                @Override
                public void onMessageTimeout(MIMCMessage mimcMessage) {

                }
            });

        }
        //群聊
        else if (StringUtils.equals(view.getContactType(), Constant.USER_TYPE_TOPIC)) {
            userManager.setGroupMessageListener(new OnReceiveGroupMessageListener() {
                @Override
                public void onMessageSuccess(MIMCGroupMessage mimcGroupMessage) {
                    MessagesBean messagesBean = userManager.tToMessagesBean(mimcGroupMessage);
                    List<MessagesBean> messages = new ArrayList<>();
                    messages.add(messagesBean);

                    showMessage(activity, view, messages, false);
                }

                @Override
                public void onMessageTimeout(MIMCGroupMessage mimcGroupMessage) {

                }
            });

        }


    }

    @Override
    public void loadHistoryMessage(final Activity activity, final ChatOpenView view) {
        KeyboardUtils.hideSoftInput(activity);
        if (StringUtils.equals(view.getContactType(), Constant.USER_TYPE_USER)) {
            userManager.pullP2PHistory(view.getContactId(), userManager.getAccount(), view.getEndTime(), view.getCount()
                    , new OnResultStringListener() {
                        @Override
                        public void onResponse(String response) {
                            view.getRefresh().setRefreshing(false);
                            MessageHistoryBean historyBean = GsonUtils.fromJson(response, MessageHistoryBean.class);
                            if (historyBean.getCode() == 200) {
                                MessageHistoryBean.DataBean data = historyBean.getData();
                                List<MessagesBean> messages = data.getMessages();

                                if (!messages.isEmpty()) {
                                    showMessage(activity, view, messages, true);
                                } else {
                                    ToastUtils.showShort("没有聊天记录");
                                }
                            }
                        }

                        @Override
                        public void onFailure(String error) {
                            view.getRefresh().setRefreshing(false);
                            LogUtils.e(error);
                        }
                    });

        }
        //群聊
        else if (StringUtils.equals(view.getContactType(), Constant.USER_TYPE_TOPIC)) {
            userManager.pullP2THistory(userManager.getAccount(), view.getContactId(), view.getEndTime(), view.getCount()
                    , new OnResultStringListener() {
                        @Override
                        public void onResponse(String response) {
                            view.getRefresh().setRefreshing(false);
                            MessageHistoryBean historyBean = GsonUtils.fromJson(response, MessageHistoryBean.class);
                            if (historyBean.getCode() == 200) {
                                MessageHistoryBean.DataBean data = historyBean.getData();
                                List<MessagesBean> messages = data.getMessages();
                                if (!messages.isEmpty()) {
                                    showMessage(activity, view, messages, true);
                                } else {
                                    ToastUtils.showShort("没有聊天记录");
                                }
                            }
                        }

                        @Override
                        public void onFailure(String error) {
                            view.getRefresh().setRefreshing(false);
                        }
                    });
        }
    }

    @Override
    public void showMessage(Activity activity, ChatOpenView view, List<MessagesBean> messages, boolean isTop) {
        final ChatAdapter adapter = view.getAdapter();
        int old_index = adapter.getItemCount();

        adapter.setType(view.getContactType());
        adapter.setData(messages, isTop);
        if (isTop) {
            adapter.notifyItemRangeChanged(0, adapter.getItemCount());
        } else {
            adapter.notifyItemRangeChanged(old_index, adapter.getItemCount());
        }

        MessagesBean messagesBean = messages.get(0);
        if (messagesBean != null)
            view.setEndTime(messagesBean.getTs());

        view.scrollBottom();
    }

    @Override
    public void sendMessage(final Activity activity, final ChatOpenView view) {
        String sendText = Objects.requireNonNull(view.getSendText().getText()).toString().trim();
        if (StringUtils.isEmpty(sendText)) {
            ToastUtils.showShort(R.string.input_something);
            return;
        }

        view.getSendText().setText(null);
        //私聊
        if (StringUtils.equals(view.getContactType(), Constant.USER_TYPE_USER)) {
            PayLoadBean payLoadBean = new PayLoadBean(view.getBizType() + "_" + MyUtils.getRandomString(8), sendText, view.getBackGround());
            //创建json
            final String payload = GsonUtils.toJson(payLoadBean);

            MessagesBean messagesBean = new MessagesBean();
            messagesBean.setTs(TimeUtils.getNowMills());
            messagesBean.setBizType(view.getBizType());
            messagesBean.setFromAccount(userManager.getAccount());
            messagesBean.setToAccount(view.getContactId());
            messagesBean.setPayload(new String(Base64.encode(payload.getBytes(), Base64.DEFAULT)));

            List<MessagesBean> messages = new ArrayList<>();
            messages.add(messagesBean);

            //显示自己发送的消息
            showMessage(activity, view, messages, false);

            userManager.sendMsg(view.getContactId(), payload.getBytes(), view.getBizType(), new OnSendMessageListener<MIMCMessage>() {
                @Override
                public void onSending(MIMCMessage mimcMessage) {
                    ToastUtils.showShort("发送中");
                }

                @Override
                public void onFailure(String info) {
                    ToastUtils.showShort("发送失败：" + info);
                }

                @Override
                public void onServerAck(MIMCServerAck serverAck) {
                    ToastUtils.showShort("发送成功");
                }
            });
        }
        //群聊
        else if (StringUtils.equals(view.getContactType(), Constant.USER_TYPE_TOPIC)) {
            PayLoadBean payLoadBean = new PayLoadBean(view.getBizType() + "_" + MyUtils.getRandomString(8), sendText, view.getBackGround());
            //创建json
            final String payload = GsonUtils.toJson(payLoadBean);

            MessagesBean messagesBean = new MessagesBean();
            messagesBean.setTs(TimeUtils.getNowMills());
            messagesBean.setBizType(view.getBizType());
            messagesBean.setFromAccount(userManager.getAccount());
            messagesBean.setPayload(new String(Base64.encode(payload.getBytes(), Base64.DEFAULT)));

            List<MessagesBean> messages = new ArrayList<>();
            messages.add(messagesBean);

            //显示自己发送的消息
            showMessage(activity, view, messages, false);

            userManager.sendGroupMsg(Long.parseLong(view.getContactId()), payload.getBytes(),
                    view.getBizType(), false, new OnSendMessageListener<MIMCGroupMessage>() {
                        @Override
                        public void onSending(MIMCGroupMessage groupMessage) {
                            ToastUtils.showShort("发送中");
                        }

                        @Override
                        public void onFailure(String info) {
                            ToastUtils.showShort("发送失败：" + info);
                        }

                        @Override
                        public void onServerAck(MIMCServerAck serverAck) {
                            ToastUtils.showShort("发送成功");
                        }
                    });
        }
    }

}
