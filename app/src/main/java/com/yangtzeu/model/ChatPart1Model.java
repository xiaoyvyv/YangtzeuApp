package com.yangtzeu.model;

import android.app.Activity;

import com.blankj.utilcode.util.ToastUtils;
import com.lib.chat.bean.ContactBean;
import com.lib.chat.common.UserManager;
import com.lib.chat.listener.receive.OnReceiveGroupMessageListener;
import com.lib.chat.listener.receive.OnReceiveMessageListener;
import com.lib.chat.listener.receive.OnReceiveUnlimitGroupMessageListener;
import com.lib.subutil.GsonUtils;
import com.xiaomi.mimc.MIMCGroupMessage;
import com.xiaomi.mimc.MIMCMessage;
import com.xiaomi.mimc.MIMCUser;
import com.yangtzeu.R;
import com.yangtzeu.http.OnResultStringListener;
import com.yangtzeu.model.imodel.IChatPart1Model;
import com.yangtzeu.ui.view.ChatPartView1;

import java.util.List;

public class ChatPart1Model implements IChatPart1Model {
    private UserManager instance;

    public ChatPart1Model() {
        instance = UserManager.getInstance();
    }

    @Override
    public void setMessageListener(final Activity activity, final ChatPartView1 view) {
        //单聊
        instance.setMessageListener(new OnReceiveMessageListener() {
            @Override
            public void onMessageSuccess(MIMCMessage mimcMessage) {
                instance.setUnRead(mimcMessage.getFromAccount(), 1);
                loadContactHistory(activity, view);
            }

            @Override
            public void onMessageTimeout(MIMCMessage mimcMessage) {
                //ToastUtils.showShort("接收消息失败");
            }
        });
        //群聊
        instance.setGroupMessageListener(new OnReceiveGroupMessageListener() {
            @Override
            public void onMessageSuccess(MIMCGroupMessage mimcGroupMessage) {
                instance.setUnRead(String.valueOf(mimcGroupMessage.getTopicId()), 1);
                loadContactHistory(activity, view);
            }

            @Override
            public void onMessageTimeout(MIMCGroupMessage mimcGroupMessage) {
                //ToastUtils.showShort("接收消息失败");
            }
        });

        //无限大群聊
        instance.setUnlimitGroupMessageListener(new OnReceiveUnlimitGroupMessageListener() {
            @Override
            public void onMessageSuccess(MIMCGroupMessage mimcGroupMessage) {
                instance.setUnRead(String.valueOf(mimcGroupMessage.getTopicId()), 1);
                loadContactHistory(activity, view);
            }

            @Override
            public void onMessageTimeout(MIMCGroupMessage mimcGroupMessage) {
                //ToastUtils.showShort("接收消息失败");
            }
        });
    }

    @Override
    public void loadContactHistory(final Activity activity, final ChatPartView1 view) {
        instance.queryContactInfo(new OnResultStringListener() {
            @Override
            public void onResponse(String response) {
                view.getRefresh().setRefreshing(false);
                ContactBean contactBean = GsonUtils.fromJson(response, ContactBean.class);
                int code = contactBean.getCode();
                if (code == 200) {
                    List<ContactBean.ContactData> data = contactBean.getData();
                    view.getAdapter().setData(data);
                    view.getAdapter().notifyDataSetChanged();

                    if (view.getAdapter().getItemCount() == 0) {
                        view.getRefresh().setBackgroundResource(R.drawable.empty_message);
                    } else {
                        view.getRefresh().setBackground(null);
                    }
                }

            }

            @Override
            public void onFailure(String error) {
                view.getRefresh().setRefreshing(false);
                ToastUtils.showShort(R.string.try_again);
            }
        });
    }
}
