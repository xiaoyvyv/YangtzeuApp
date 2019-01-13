package com.yangtzeu.model;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.lib.mob.im.IMManager;
import com.lib.subutil.GsonUtils;
import com.mob.imsdk.MobIM;
import com.mob.imsdk.MobIMCallback;
import com.mob.imsdk.MobIMMessageReceiver;
import com.mob.imsdk.MobIMReceiver;
import com.mob.imsdk.model.IMConversation;
import com.mob.imsdk.model.IMMessage;
import com.yangtzeu.R;
import com.yangtzeu.entity.GroupListBean;
import com.yangtzeu.http.OkHttp;
import com.yangtzeu.http.OnResultStringListener;
import com.yangtzeu.model.imodel.IChatModel;
import com.yangtzeu.ui.view.ChatView;
import com.yangtzeu.url.Url;
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
                boolean isForbidden = SPUtils.getInstance("user_info").getBoolean("group_notice", false);
                if (!isForbidden) {
                    MyUtils.mVibrator(activity, 1000);
                }
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


    @Override
    public void loadAllGroupInfo(final Activity activity, ChatView view) {
        OkHttp.do_Get(Url.Yangtzeu_Group_List, new OnResultStringListener() {
            @Override
            @SuppressLint({"InflateParams", "SetTextI18n"})
            public void onResponse(String response) {
                GroupListBean groupListBean = GsonUtils.fromJson(response, GroupListBean.class);
                List<GroupListBean.DataBean> data = groupListBean.getData();

                View dialog_view = LayoutInflater.from(activity).inflate(R.layout.activity_chat_dialog, null);
                final AlertDialog dialog = new AlertDialog.Builder(activity)
                        .setView(dialog_view)
                        .create();
                dialog.show();
                dialog.setCanceledOnTouchOutside(false);

                LinearLayout container = dialog_view.findViewById(R.id.container);
                container.removeAllViews();
                for (int i = 0; i < data.size(); i++) {
                    View item = LayoutInflater.from(activity).inflate(R.layout.activity_chat_dialog_item, null);
                    container.addView(item);
                    final GroupListBean.DataBean dataBean = data.get(i);
                    TextView group_name = item.findViewById(R.id.group_name);

                    final TextView group_id = item.findViewById(R.id.group_id);
                    TextView group_desc = item.findViewById(R.id.group_desc);
                    TextView group_time = item.findViewById(R.id.group_time);
                    Switch aSwitch = item.findViewById(R.id.isNotice);
                    boolean isForbidden = SPUtils.getInstance("user_info").getBoolean("group_notice", false);
                    aSwitch.setChecked(isForbidden);

                    LinearLayout onClick = item.findViewById(R.id.onClick);

                    group_name.setText(dataBean.getName());
                    group_id.setText("群ID："+dataBean.getId());
                    group_desc.setText("群描述："+dataBean.getDesc());
                    group_time.setText("创建时间：" + dataBean.getCreate());

                    onClick.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            IMManager.joinGroup(dataBean.getId());
                        }
                    });
                    aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            SPUtils.getInstance("user_info").put("group_notice" , isChecked);
                        }
                    });
                }
            }
            @Override
            public void onFailure(String error) {

            }
        });

    }

}
