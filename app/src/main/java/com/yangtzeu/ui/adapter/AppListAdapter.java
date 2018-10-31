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
import com.yangtzeu.entity.AppBean;
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

public class AppListAdapter extends RecyclerView.Adapter<AppListAdapter.ViewHolder> {
    private Context context;
    private List<AppBean.AppListBean> appBeans = new ArrayList<>();

    public AppListAdapter(Context context) {
        this.context = context;
    }

    public void setData(AppBean appBean) {
        this.appBeans.addAll(appBean.getAppList());
    }

    public void clear() {
        appBeans.clear();
    }


    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.activity_app_list_item, parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AppBean.AppListBean itemBean = appBeans.get(position);
        final int size = appBeans.size();
        final String url =itemBean.getUrl();
        final String name =itemBean.getName();
        final String icon =itemBean.getIcon();
        final String author =itemBean.getAuthor();
        final String message =itemBean.getMessage();

        holder.name.setText(name);
        holder.author.setText(author);
        Glide.with(context).load(icon).into(holder.icon);
        holder.message.setText(message);

        holder.mLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyUtils.openUrl(context,url);
            }
        });
    }
    @Override
    public long getItemId(int i) {
        return super.getItemId(i);
    }

    @Override
    public int getItemCount() {
        return appBeans.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView icon ;
        TextView name;
        TextView message ;
        TextView author ;
        LinearLayout mLinearLayout;
        ViewHolder(View view) {
            super(view);
            icon = view.findViewById(R.id.icon);
            name = view.findViewById(R.id.name);
            message = view.findViewById(R.id.message);
            author = view.findViewById(R.id.author);
            mLinearLayout = view.findViewById(R.id.mLinearLayout);
        }
    }

}