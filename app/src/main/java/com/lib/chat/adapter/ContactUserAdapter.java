package com.lib.chat.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lib.chat.bean.ContactUserBean;
import com.lib.chat.common.Constant;
import com.yangtzeu.R;
import com.yangtzeu.utils.MyUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ContactUserAdapter extends RecyclerView.Adapter<ContactUserAdapter.ViewHolder>  {
    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private List<ContactUserBean> contactUserBeans = new ArrayList<>();

    public ContactUserAdapter(Context context) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    public void setData(List<ContactUserBean> contactData) {
        this.contactUserBeans = contactData;
    }

    @NonNull
    @Override
    public ContactUserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.fragment_chat_part2_item, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint({"SetTextI18n", "ClickableViewAccessibility"})
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        ContactUserBean contactData = this.contactUserBeans.get(position);

        final String name = contactData.getName();
        final String note = contactData.getNote();
        final String number = contactData.getNumber();
        final String qq = contactData.getQq();
        final String time =contactData.getTime();

        holder.name.setText(name );
        holder.message.setText(note);
        holder.time.setText(time);

        MyUtils.loadImage(mContext, holder.header, MyUtils.getQQHeader(qq));

        holder.onClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyUtils.chatOnline(mContext, number, Constant.USER_TYPE_USER);
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return contactUserBeans.size();
    }

    public void clear() {
        contactUserBeans.clear();
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


