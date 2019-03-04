package com.lib.chat.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.Utils;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.lib.chat.bean.ChatInfoBean;
import com.lib.chat.bean.MessagesBean;
import com.lib.chat.bean.PayLoadBean;
import com.lib.chat.common.Constant;
import com.lib.chat.common.UserManager;
import com.lib.chat.listener.send.OnSendMessageListener;
import com.lib.subutil.GsonUtils;
import com.lib.yun.Base64;
import com.xiaomi.mimc.MIMCMessage;
import com.xiaomi.mimc.MIMCServerAck;
import com.yangtzeu.R;
import com.yangtzeu.http.OnResultStringListener;
import com.yangtzeu.ui.activity.ChatOpenActivity;
import com.yangtzeu.ui.activity.InfoActivity;
import com.yangtzeu.ui.fragment.ChatFragment3;
import com.yangtzeu.utils.MyUtils;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {
    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private List<MessagesBean> messagesBeans = new ArrayList<>();
    private static final int TYPE_RECEIVED = 0;
    private static final int TYPE_SEND = 1;
    private String contactType;

    public ChatAdapter(Context context) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    public void setType(String contactType) {
        this.contactType = contactType;
    }
    public void setData(List<MessagesBean> messagesBeans, boolean isTop) {
        if (isTop) {
            this.messagesBeans.addAll(0, messagesBeans);
        } else {
            this.messagesBeans.addAll(messagesBeans);
        }
    }


    @NonNull
    @Override
    public ChatAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == TYPE_SEND) {
            view = mLayoutInflater.inflate(R.layout.activity_chat_open_send, parent, false);
            return new ViewHolder(view);
        } else {
            view = mLayoutInflater.inflate(R.layout.activity_chat_open_receive, parent, false);
            return new ViewHolder(view);
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final ChatAdapter.ViewHolder holder, int position) {
        final MessagesBean messagesBean = this.messagesBeans.get(position);

        final String bizType = messagesBean.getBizType();
        //消息发送者id
        final String fromAccount = messagesBean.getFromAccount();

        //时间
        String timestamp = com.blankj.utilcode.util.TimeUtils.millis2String((messagesBean.getTs()));

        //Base64解析payLoad内容
        String payload = messagesBean.getPayload();

        try {
            payload = new String(Base64.decode(payload.getBytes(), Base64.DEFAULT), StandardCharsets.UTF_8);
        } catch (Exception e) {
            payload = messagesBean.getPayload();
        }

        String message = payload;
        String name;
        String qq;
        String msgId = null;

        //解析payload包含内容
        Gson GSON = new Gson();
        PayLoadBean payLoadBean = null;
        try {
            payLoadBean = GSON.fromJson(payload, PayLoadBean.class);
        } catch (JsonSyntaxException e) {
            message = messagesBean.getPayload();
        }

        if (ObjectUtils.isNotEmpty(payLoadBean)) {
            String background = payLoadBean.getBackground();
            name = payLoadBean.getName();
            qq = payLoadBean.getQq();
            message = payLoadBean.getText();
            msgId = payLoadBean.getMsgId();

            holder.account.setText(name);

            //加载头像
            if (StringUtils.isEmpty(qq)) {
                MyUtils.loadImage(Utils.getApp(), holder.header, R.mipmap.holder);
            } else {
                MyUtils.loadImage(Utils.getApp(), holder.header, MyUtils.getQQHeader(qq));
            }

            if (StringUtils.isEmpty(background)) {
                holder.cardView.setCardBackgroundColor(Color.WHITE);
            } else {
                holder.cardView.setCardBackgroundColor(Color.parseColor(background));
            }
        } else {
            holder.account.setText(fromAccount);
            MyUtils.loadImage(mContext, holder.header, R.mipmap.holder);
            holder.cardView.setCardBackgroundColor(Color.WHITE);
        }
        holder.message.setText(message);
        holder.time.setText(timestamp);
        //头像点击事件
        holder.header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StringUtils.equals(contactType, Constant.USER_TYPE_TOPIC)) {
                    if (StringUtils.equals(fromAccount, UserManager.getInstance().getAccount())) {
                        Intent intent = new Intent(mContext, InfoActivity.class);
                        intent.putExtra("id", UserManager.getInstance().getAccount());
                        intent.putExtra("name", SPUtils.getInstance("user_info").getString("name"));
                        MyUtils.startActivity(intent);
                    } else {
                        Intent intent = new Intent(mContext, ChatOpenActivity.class);
                        intent.putExtra("type", Constant.USER_TYPE_USER);
                        intent.putExtra("id", fromAccount);
                        MyUtils.startActivity(intent);
                        ((Activity) mContext).finish();
                    }
                }
            }
        });

        final String finalMessage = message;
        final String finalMsgId = msgId;
        holder.onClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //加群申请
                if (StringUtils.equals(bizType, Constant.ADD_GROUP)) {
                    String author = null;
                    String topic_id = null;

                    if (!ObjectUtils.isEmpty(finalMsgId)) {
                        String[] s = finalMsgId.split("_");
                        if (!ObjectUtils.isEmpty(s)) {
                            if (s.length == 2) {
                                author = s[0];
                                topic_id = s[1];
                            }
                        }
                    }

                    //群管理员
                    if (StringUtils.equals(author, UserManager.getInstance().getAccount())) {
                        PopupMenu menu = new PopupMenu(mContext, holder.onClick);
                        menu.getMenu().add(0, 0, 0, "同意申请");
                        menu.getMenu().add(0, 1, 0, "拒绝申请");

                        final String finalTopic_id = topic_id;
                        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                String message_result;
                                int i = item.getItemId();
                                if (i == 0) {
                                    message_result = finalMessage + "\n\n管理员审核结果：同意申请";
                                    if (finalTopic_id == null) {
                                        message_result = finalMessage + "\n\n管理员审核结果：群id无效，请重新申请";
                                    } else {
                                        UserManager.getInstance().joinGroup(finalTopic_id, fromAccount, new OnResultStringListener() {
                                            @Override
                                            public void onResponse(String response) {
                                                ChatInfoBean infoBean = GsonUtils.fromJson(response, ChatInfoBean.class);
                                                int code = infoBean.getCode();
                                                if (code != 200) {
                                                    ToastUtils.showLong("该群不存在，请重试（" + infoBean.getMessage() + ")");
                                                } else {
                                                    ToastUtils.showLong("操作：" + infoBean.getMessage());
                                                    if (ChatFragment3.presenter != null) {
                                                        ChatFragment3.presenter.loadContacts();
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onFailure(String error) {
                                                ToastUtils.showLong("操作：失败！");
                                            }
                                        });
                                    }
                                } else {
                                    message_result = finalMessage + "\n\n管理员审核结果：拒绝申请";
                                }
                                //回复申请者审核结果
                                PayLoadBean payLoadBean = new PayLoadBean(Constant.ADD_GROUP + "_" + MyUtils.getRandomString(8), message_result, "#dddddd");
                                String payload_json = GsonUtils.toJson(payLoadBean);
                                UserManager.getInstance().sendMsg(fromAccount, payload_json.getBytes(), Constant.ADD_GROUP, new OnSendMessageListener<MIMCMessage>() {
                                    @Override
                                    public void onSending(MIMCMessage mimcMessage) {
                                        ToastUtils.showShort("入群申请审核结果发送中");
                                    }

                                    @Override
                                    public void onFailure(String info) {
                                        ToastUtils.showShort("入群申请审核结果发送失败");
                                    }

                                    @Override
                                    public void onServerAck(MIMCServerAck serverAck) {
                                        ToastUtils.showShort("入群申请审核结果发送成功");
                                    }
                                });
                                return false;
                            }
                        });
                        menu.show();
                    }
                    //申请者
                    else {
                        ToastUtils.showShort(holder.message.getText());
                    }
                }
                //消息点击操作
                //TODO 消息点击操作

            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KeyboardUtils.hideSoftInput((Activity) mContext);
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        if (messagesBeans.get(position).getFromAccount().equals(UserManager.getInstance().getAccount())) {
            return TYPE_SEND;
        }
        return TYPE_RECEIVED;
    }

    @Override
    public int getItemCount() {
        return messagesBeans.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView message;
        TextView account;
        TextView time;
        LinearLayout onClick;
        ImageView header;
        ImageView image;
        CardView cardView;


        ViewHolder(View view) {
            super(view);
            cardView = view.findViewById(R.id.cardView);
            time = view.findViewById(R.id.time);
            message = view.findViewById(R.id.message);
            account = view.findViewById(R.id.account);
            onClick = view.findViewById(R.id.onClick);
            header = view.findViewById(R.id.header);
            image = view.findViewById(R.id.image);
        }
    }
}


