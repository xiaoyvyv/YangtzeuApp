package com.yangtzeu.model;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.lib.chat.bean.ContactGroupBean;
import com.lib.chat.bean.CreateGroupBean;
import com.lib.chat.bean.PayLoadBean;
import com.lib.chat.common.Constant;
import com.lib.chat.common.UserManager;
import com.lib.chat.listener.send.OnSendMessageListener;
import com.lib.subutil.GsonUtils;
import com.xiaomi.mimc.MIMCMessage;
import com.xiaomi.mimc.MIMCServerAck;
import com.yangtzeu.R;
import com.yangtzeu.entity.GroupListBean;
import com.yangtzeu.http.OkHttp;
import com.yangtzeu.http.OnResultStringListener;
import com.yangtzeu.model.imodel.IChatModel;
import com.yangtzeu.ui.fragment.ChatFragment3;
import com.yangtzeu.ui.view.ChatView;
import com.yangtzeu.url.Url;

import java.util.List;

public class ChatModel implements IChatModel {
    final UserManager instance;

    public ChatModel() {
        instance = UserManager.getInstance();
    }

    @Override
    public void createGroup(Activity activity, ChatView view) {
        final EditText edit = new EditText(activity);
        int dp20 = ConvertUtils.dp2px(25);
        edit.setPadding(dp20, dp20 / 2, dp20, 0);
        edit.setTextSize(15);
        edit.setHint(R.string.trip);
        edit.setBackgroundResource(R.drawable.ic_chat_bg);
        AlertDialog dialog = new AlertDialog.Builder(activity)
                .setTitle(R.string.input_group_name)
                .setView(edit)
                .setNegativeButton(R.string.clear,null)
                .setPositiveButton(R.string.done, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name = edit.getText().toString().trim();
                        if (name.isEmpty()) {
                            ToastUtils.showShort("未输入");
                            return;
                        }
                        instance.createGroup(name, instance.getAccount(), new OnResultStringListener() {
                            @Override
                            public void onResponse(String response) {
                                CreateGroupBean bean = GsonUtils.fromJson(response, CreateGroupBean.class);
                                if (bean.getCode() == 200) {
                                    CreateGroupBean.DataBean data = bean.getData();
                                    ContactGroupBean topicInfo = data.getTopicInfo();
                                    LogUtils.e(GsonUtils.toJson(topicInfo));

                                    if (ChatFragment3.presenter != null) {
                                        ChatFragment3.presenter.loadContacts();
                                    }
                                } else {
                                    ToastUtils.showShort(bean.getMessage());
                                }
                            }

                            @Override
                            public void onFailure(String error) {
                                ToastUtils.showShort(error);
                            }
                        });
                    }
                }).create();
        dialog.show();
    }

    @Override
    public void loadYangtzeuGroupInfo(final Activity activity, final ChatView chatView) {
        OkHttp.do_Get(Url.Yangtzeu_Group_List, new OnResultStringListener() {
            @Override
            @SuppressLint({"InflateParams", "SetTextI18n"})
            public void onResponse(String response) {
                GroupListBean groupListBean = GsonUtils.fromJson(response, GroupListBean.class);
                final List<GroupListBean.DataBean> data = groupListBean.getData();

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

                    boolean isHide = SPUtils.getInstance("group_notice").getBoolean(dataBean.getId(), false);
                    aSwitch.setChecked(isHide);

                    LinearLayout onClick = item.findViewById(R.id.onClick);

                    group_name.setText(dataBean.getName());
                    group_id.setText("群ID："+dataBean.getId());
                    group_id.setText("群主ID："+dataBean.getAuthor());
                    group_desc.setText("群描述："+dataBean.getDesc());
                    group_time.setText("创建时间：" + dataBean.getCreate());

                    onClick.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            //查询是否在群里面
                            boolean is_in_group = SPUtils.getInstance("group_list").getBoolean(dataBean.getId(), false);
                            if (is_in_group) {
                                ToastUtils.showShort("您已经加入了此群");
                                return;
                            }

                            String sendText = "用户：" + instance.getAccount() + "\n申请入群：" + dataBean.getName();
                            PayLoadBean payLoadBean = new PayLoadBean( dataBean.getAuthor()+"_"+dataBean.getId(), sendText,"#dddddd");
                            String payload_json = GsonUtils.toJson(payLoadBean);
                            instance.sendMsg(dataBean.getAuthor(), payload_json.getBytes(), Constant.ADD_GROUP, new OnSendMessageListener<MIMCMessage>() {
                                @Override
                                public void onSending(MIMCMessage mimcMessage) {
                                    ToastUtils.showShort("加群申请发送中");
                                }

                                @Override
                                public void onFailure(String info) {
                                    ToastUtils.showShort("加群申请发送失败");
                                }

                                @Override
                                public void onServerAck(MIMCServerAck serverAck) {
                                    ToastUtils.showShort("加群申请发送成功，等待管理员审核");
                                }
                            });
                        }
                    });
                    aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            SPUtils.getInstance("group_notice").put(dataBean.getId(), isChecked);
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
