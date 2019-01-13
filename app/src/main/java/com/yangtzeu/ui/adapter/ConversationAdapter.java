package com.yangtzeu.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.lib.mob.im.IMManager;
import com.mob.imsdk.MobIM;
import com.mob.imsdk.model.IMConversation;
import com.mob.imsdk.model.IMGroup;
import com.mob.imsdk.model.IMMessage;
import com.mob.imsdk.model.IMReminder;
import com.mob.imsdk.model.IMUser;
import com.yangtzeu.R;
import com.yangtzeu.ui.activity.ChatDetailsActivity;
import com.yangtzeu.utils.MyUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


/**
 * Created by Administrator on 2018/4/12.
 *
 * @author 王怀玉
 * @explain MoreAdapter
 */

public class ConversationAdapter extends RecyclerView.Adapter<ConversationAdapter.ViewHolder> {
    private Context context;
    private List<IMConversation> imConversations = new ArrayList<>();

    public ConversationAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<IMConversation> imConversations) {
        this.imConversations = imConversations;
    }

    public void clear() {
        imConversations.clear();
        notifyDataSetChanged();
    }


    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.activity_conversation_item, parent,false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final IMConversation itemBean = imConversations.get(position);

        final int type = itemBean.getType();
        long createTime = itemBean.getCreateTime();
        int unreadMsgCount = itemBean.getUnreadMsgCount();
        IMMessage lastMessage = itemBean.getLastMessage();

        holder.message.setText(lastMessage.getBody());
        holder.time.setText(TimeUtils.millis2String(createTime));

        if (unreadMsgCount != 0) {
            holder.unRead.setVisibility(View.VISIBLE);
            holder.unRead.setText(String.valueOf(unreadMsgCount));
        } else {
            holder.unRead.setVisibility(View.GONE);
        }
        String targetId = null;
        switch (type) {
            case IMConversation.TYPE_USER:
                IMUser imUser = itemBean.getOtherInfo();
                MyUtils.loadImage(context, holder.icon, imUser.getAvatar());
                holder.name.setText(imUser.getNickname());
                targetId = imUser.getId();
                break;
            case IMConversation.TYPE_GROUP:
                IMGroup groupInfo = itemBean.getGroupInfo();
                MyUtils.loadImage(context, holder.icon, groupInfo.getOwnerInfo().getAvatar());
                holder.name.setText(groupInfo.getName());
                targetId = groupInfo.getId();
                break;
            case IMConversation.TYPE_REMINDER:
                IMReminder reminderInfo = itemBean.getReminderInfo();
                MyUtils.loadImage(context, holder.icon, reminderInfo.getAvatar());
                holder.name.setText(reminderInfo.getName());
                targetId = reminderInfo.getId();
                break;
        }
        final String finalTargetId = targetId;
        holder.mLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //targetId - 目标id（群聊为群的id，私聊为对方id）
                MobIM.getChatManager().markConversationAllMessageAsRead(finalTargetId, type);

                IMManager.chat(finalTargetId, type);
            }
        });

        holder.mLinearLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                boolean b = MobIM.getChatManager().delConversation(finalTargetId, type);
                boolean c = MobIM.getChatManager().clearConversationMessage(finalTargetId, type);
                if (b||c) {
                    ToastUtils.showShort("删除成功");
                    imConversations.remove(position);
                    ConversationAdapter.this.notifyItemRemoved(position);
                    ConversationAdapter.this.notifyDataSetChanged();
                } else {
                    ToastUtils.showShort("删除失败");
                }
                return true;
            }
        });

    }
    @Override
    public long getItemId(int i) {
        return super.getItemId(i);
    }

    @Override
    public int getItemCount() {
        return imConversations.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView icon ;
        TextView name;
        TextView message ;
        TextView time ;
        TextView unRead ;
        LinearLayout mLinearLayout;
        ViewHolder(View view) {
            super(view);
            icon = view.findViewById(R.id.icon);
            name = view.findViewById(R.id.name);
            message = view.findViewById(R.id.message);
            unRead = view.findViewById(R.id.unRead);
            time = view.findViewById(R.id.time);
            mLinearLayout = view.findViewById(R.id.mLinearLayout);
        }
    }

}