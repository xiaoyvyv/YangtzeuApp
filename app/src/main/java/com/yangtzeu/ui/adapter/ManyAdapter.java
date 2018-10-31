package com.yangtzeu.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.yangtzeu.R;
import com.yangtzeu.entity.ManyBean;
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

public class ManyAdapter extends RecyclerView.Adapter<ManyAdapter.ViewHolder> {
    private Context context;
    private List<ManyBean.DataBean> appBeans = new ArrayList<>();

    public ManyAdapter(Context context) {
        this.context = context;
    }

    public void setData(ManyBean bean) {
        this.appBeans = bean.getData();
    }

    public void clear() {
        appBeans.clear();
        notifyItemRangeChanged(0, getItemCount());
    }


    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.fragment_many_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ManyBean.DataBean itemBean = appBeans.get(position);
        final String url = itemBean.getUrl();
        final String title = itemBean.getTitle();
        final String icon = itemBean.getIcon();

        holder.image.setText(icon);

        holder.title.setText(title);
        holder.OnClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    MyUtils.openUrl(context, url, true);
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
        TextView image;
        TextView title;
        LinearLayout OnClick;

        ViewHolder(View view) {
            super(view);
            image = view.findViewById(R.id.image);
            title = view.findViewById(R.id.title);
            OnClick = view.findViewById(R.id.OnClick);
        }
    }


}