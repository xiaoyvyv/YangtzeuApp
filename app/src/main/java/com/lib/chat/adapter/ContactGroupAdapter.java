package com.lib.chat.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.StringUtils;
import com.lib.chat.bean.ContactGroupBean;
import com.lib.chat.bean.GroupExtraBean;
import com.lib.chat.common.Constant;
import com.lib.subutil.GsonUtils;
import com.yangtzeu.R;
import com.yangtzeu.ui.activity.ChatOpenActivity;
import com.yangtzeu.utils.MyUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ContactGroupAdapter extends RecyclerView.Adapter<ContactGroupAdapter.ViewHolder> {
    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private List<ContactGroupBean> groupBeans = new ArrayList<>();

    public ContactGroupAdapter(Context context) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);


        //清空我加入的群信息，重新统计
        SPUtils.getInstance("group_list").clear();
    }

    public void setData(List<ContactGroupBean> contactData) {
        this.groupBeans = contactData;
    }

    @NonNull
    @Override
    public ContactGroupAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.fragment_chat_part3_item, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint({"SetTextI18n", "ClickableViewAccessibility"})
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        ContactGroupBean contactData = this.groupBeans.get(position);

        final String topicName = contactData.getTopicName();
        final String extra = String.valueOf(contactData.getExtra());
        final String topicId = contactData.getTopicId();
        final String bulletin = String.valueOf(contactData.getBulletin());
        final String ownerUuid = contactData.getOwnerUuid();
        final String ownerAccount = contactData.getOwnerAccount();

        holder.name.setText(topicName);
        if (StringUtils.equals(bulletin, "null")) {
            holder.message.setText("公告：该群还未发布公告");
        } else {
            holder.message.setText("公告：" + bulletin);
        }

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

        holder.onClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ChatOpenActivity.class);
                intent.putExtra("type",  Constant.USER_TYPE_TOPIC);
                intent.putExtra("id", topicId);
                intent.putExtra("name", topicName);
                MyUtils.startActivity(intent);
            }
        });

        //保存我加入的群
        SPUtils.getInstance("group_list").put(topicId, true);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return groupBeans.size();
    }

    public void clear() {
        groupBeans.clear();
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView message;
        TextView time;
        ImageView header;
        LinearLayout onClick;

        ViewHolder(View view) {
            super(view);
            header = view.findViewById(R.id.header);
            name = view.findViewById(R.id.name);
            message = view.findViewById(R.id.message);
            time = view.findViewById(R.id.time);
            onClick = view.findViewById(R.id.onClick);
        }
    }

}


