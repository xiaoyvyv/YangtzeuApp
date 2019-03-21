package com.lib.chat.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lib.chat.bean.CreateGroupBean;
import com.lib.chat.common.Constant;
import com.yangtzeu.R;
import com.yangtzeu.utils.MyUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class GroupInnerUserAdapter extends RecyclerView.Adapter<GroupInnerUserAdapter.ViewHolder> {
    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private List<CreateGroupBean.DataBean.MembersBean> members = new ArrayList<>();
    private CreateGroupBean.DataBean dataBean;

    public GroupInnerUserAdapter(Context context) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    public void setData(CreateGroupBean.DataBean dataBean) {
        this.dataBean = dataBean;
        this.members = dataBean.getMembers();
    }


    @NonNull
    @Override
    public GroupInnerUserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = mLayoutInflater.inflate(R.layout.activity_chat_group_info_item, parent, false);
        return new ViewHolder(view);

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final GroupInnerUserAdapter.ViewHolder holder, int position) {
        CreateGroupBean.DataBean.MembersBean member = this.members.get(position);
        final String account = member.getAccount();
        if (account.equals(dataBean.getTopicInfo().getOwnerAccount())) {
            holder.author.setText("群主");
        } else {
            holder.author.setText(null);
        }

        holder.number.setText(account);
        MyUtils.loadImage(mContext, holder.header, R.mipmap.holder);


        holder.onClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyUtils.chatOnline(mContext, account, Constant.USER_TYPE_USER);
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return members.size();
    }

    public void clear() {
        members.clear();
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView author;
        TextView number;
        LinearLayout onClick;
        ImageView header;


        ViewHolder(View view) {
            super(view);
            author = view.findViewById(R.id.author);
            number = view.findViewById(R.id.number);
            onClick = view.findViewById(R.id.onClick);
            header = view.findViewById(R.id.header);
        }
    }
}


