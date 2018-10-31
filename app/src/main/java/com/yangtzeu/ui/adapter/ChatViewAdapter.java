package com.yangtzeu.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yangtzeu.R;
import com.yangtzeu.entity.ChatBean;
import com.yangtzeu.utils.MyUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by Administrator on 2018/4/12.
 *
 * @author 王怀玉
 * @explain MoreAdapter
 */

public class ChatViewAdapter extends RecyclerView.Adapter<ChatViewAdapter.ViewHolder> {
    private Context context;
    private List<ChatBean> chatBeans = new ArrayList<>();

    public ChatViewAdapter(Context context) {
        this.context = context;
    }

    public void sendMessage(List<ChatBean> chatBeans) {
        int old_index = getItemCount();
        this.chatBeans.addAll(chatBeans);
        notifyItemRangeChanged(old_index, getItemCount());
    }

    public void sendMessage(ChatBean chatBeans) {
        int old_index = getItemCount();
        this.chatBeans.add(chatBeans);
        notifyItemRangeChanged(old_index, getItemCount());
    }

    public void clear() {
        chatBeans.clear();
        notifyItemRangeChanged(0, getItemCount());
    }


    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType ==0) {
            return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.activity_chat_details_item_l, parent,false));
        } else {
            return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.activity_chat_details_item_r, parent,false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
         ChatBean itemBean = chatBeans.get(position);

        final String name =itemBean.getUser().getName();
        final String icon =itemBean.getUser().getIcon();
        final String id =itemBean.getUser().getId();
        final String message =itemBean.getText();
        final String time =itemBean.getTime();
        final int state =itemBean.getState();
        final int type =itemBean.getType();

        holder.name.setText(name);
        holder.time.setText(time);
        Glide.with(context).load(icon).into(holder.icon);
        holder.message.setText(message);

        holder.mLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
    @Override
    public long getItemId(int i) {
        return super.getItemId(i);
    }

    @Override
    public int getItemCount() {
        return chatBeans.size();
    }

    @Override
    public int getItemViewType(int position) {
        ChatBean itemBean = chatBeans.get(position);
        if (itemBean.isRight()) {
            return 1;
        } else {
            return 0;
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView icon ;
        TextView name;
        TextView message ;
        ImageView image ;
        TextView time;
        LinearLayout mLinearLayout;
        ViewHolder(View view) {
            super(view);
            image = view.findViewById(R.id.image);
            icon = view.findViewById(R.id.icon);
            name = view.findViewById(R.id.name);
            message = view.findViewById(R.id.message);
            time = view.findViewById(R.id.time);
            mLinearLayout = view.findViewById(R.id.mLinearLayout);
        }
    }

}