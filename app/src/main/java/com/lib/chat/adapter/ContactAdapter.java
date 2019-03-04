package com.lib.chat.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.Utils;
import com.lib.chat.bean.ContactBean;
import com.lib.chat.bean.GroupExtraBean;
import com.lib.chat.bean.PayLoadBean;
import com.lib.chat.common.Constant;
import com.lib.chat.common.UserManager;
import com.lib.chat.listener.handler.MessageHandler;
import com.lib.subutil.GsonUtils;
import com.lib.yun.Base64;
import com.yangtzeu.R;
import com.yangtzeu.ui.activity.ChatOpenActivity;
import com.yangtzeu.ui.fragment.ChatFragment1;
import com.yangtzeu.utils.MyUtils;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {
    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private List<ContactBean.ContactData> contactDatas = new ArrayList<>();

    public ContactAdapter(Context context) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
    }


    public void setData(List<ContactBean.ContactData> contactData) {
        List<ContactBean.ContactData> newListBean = new ArrayList<>();
        //过滤删除标记的会话
        for (ContactBean.ContactData contact : contactData) {
            String extra = contact.getExtra();
            if (!extra.equals(Constant.DELETE)) {
                newListBean.add(contact);
            }
        }
        contactData.clear();
        this.contactDatas = newListBean;
    }

    @NonNull
    @Override
    public ContactAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.fragment_chat_part1_item, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint({"SetTextI18n", "ClickableViewAccessibility"})
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final ContactBean.ContactData contactData = this.contactDatas.get(position);
        //会话类型
        final String userType = contactData.getUserType();
        //（私聊为对方uuid，群聊为群的id）
        final String contact_id = contactData.getId();
        //（私聊为对方id，群聊为群name）
        final String contact_name = contactData.getName();

        //时间
        String timestamp = TimeUtils.millis2String(Long.parseLong(contactData.getTimestamp()));


        //最后一条消息
        final ContactBean.ContactData.LastMessageBean lastMessage = contactData.getLastMessage();
        //最后一条消息发布者id
        String fromAccount = lastMessage.getFromAccount();
        //当前用户id
        String meAccount = UserManager.getInstance().getAccount();
        //Base64加密消息内容
        String base64_payload = lastMessage.getPayload();

        //解密Base64消息内容
        byte[] decode = Base64.decode(base64_payload.getBytes(), Base64.DEFAULT);
        //json消息内容
        String payload_str = new String(decode, StandardCharsets.UTF_8);
        PayLoadBean payLoadBean = GsonUtils.fromJson(payload_str, PayLoadBean.class);

        String onwer_message;
        String onwer_name;
        if (payLoadBean != null) {
            //最后一条消息发布者：内容
            onwer_message = payLoadBean.getText();
            //最后一条消息发布者：姓名
            onwer_name = payLoadBean.getName();
        } else {
            onwer_message =payload_str;
            onwer_name =fromAccount;
        }

        //群或私聊的唯一id
        final String chat_id;

        //类型为私聊
        if (userType.equals(Constant.USER_TYPE_USER)) {
            chat_id = contact_name;
            //设置私聊对方昵称
            holder.name.setText(chat_id);
            //加载用户头像
            MyUtils.loadImage(Utils.getApp(), holder.header, R.mipmap.holder);

        }
        //类型为群聊
        else {
            chat_id = contact_id;
            //设置群名
            holder.name.setText(mContext.getString(R.string.group_info) + contact_name);

            String extra = contactData.getExtra();
            //加载群头像
            String group_header = null;
            try {
                GroupExtraBean groupExtraBean = GsonUtils.getGson().fromJson(extra, GroupExtraBean.class);
                if (ObjectUtils.isNotEmpty(groupExtraBean)) {
                    List<String> data = groupExtraBean.getData();
                    if (ObjectUtils.isEmpty(data)) {
                        group_header = data.get(0);
                    }
                }
            } catch (Exception ignored) {}
            if (StringUtils.isEmpty(group_header)) {
                group_header = "http://whysroom.oss-cn-beijing.aliyuncs.com/yangtzeu/normal/group_banner.jpg";
            }
            MyUtils.loadImage(mContext, holder.header,group_header);
        }


        //设置显示最后一条消息的发布者
        if (StringUtils.equals(fromAccount, meAccount)) {
            holder.message.setText("我：" + onwer_message);
        } else {
            holder.message.setText(onwer_name + "：" + onwer_message);
        }

        //设置时间
        holder.time.setText(timestamp);

        //设置显示未读数目
        long unRead = UserManager.getInstance().getUnRead(chat_id);
        holder.unReadView.setText(String.valueOf(unRead));

        if (unRead == 0) {
            holder.unReadView.setVisibility(View.GONE);
        } else {
            holder.unReadView.setVisibility(View.VISIBLE);
        }

        holder.onClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.unReadView.setVisibility(View.GONE);

                Intent intent = new Intent(mContext, ChatOpenActivity.class);
                intent.putExtra("type", userType);
                intent.putExtra("id", chat_id);
                intent.putExtra("name", contact_name);
                MyUtils.startActivity(intent);

                //刷新统计未读消息总数目
                MessageHandler.sendReceiveMessageBroadcast();
            }
        });

        holder.onClick.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                PopupMenu menu = new PopupMenu(mContext, holder.onClick);
                menu.getMenu().add("删除此会话").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        onRemove(position,userType,chat_id);
                        return false;
                    }
                });
                menu.show();
                return true;
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return contactDatas.size();
    }

    private void onRemove(int position, String userType, String chat_id) {
        //移除数据
        contactDatas.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount());

        //私聊
        if (userType.equals(Constant.USER_TYPE_USER)) {
            UserManager.getInstance().setContactVisibility(chat_id, Constant.DELETE);
        }
        //群聊
        else {
            UserManager.getInstance().setGroupContactVisibility(chat_id, Constant.DELETE);
        }

        if (getItemCount() == 0) {
            ChatFragment1.refresh.setBackgroundResource(R.drawable.empty_message);
        } else {
            ChatFragment1.refresh.setBackground(null);
        }

        //删除后，设置当前会话全部消息已读
        UserManager.getInstance().setUnReadNone(chat_id);
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView message;
        TextView time;
        TextView unReadView;
        ImageView header;
        LinearLayout onClick;

        ViewHolder(View view) {
            super(view);
            header = view.findViewById(R.id.header);
            name = view.findViewById(R.id.name);
            unReadView = view.findViewById(R.id.messageImage);
            message = view.findViewById(R.id.message);
            time = view.findViewById(R.id.time);
            onClick = view.findViewById(R.id.onClick);
        }
    }

}


