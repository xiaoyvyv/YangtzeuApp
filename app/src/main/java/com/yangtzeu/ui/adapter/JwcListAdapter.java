package com.yangtzeu.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yangtzeu.R;
import com.yangtzeu.entity.JwcListBean;
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

public class JwcListAdapter extends RecyclerView.Adapter<JwcListAdapter.ViewHolder> {
    private Context context;
    private List<JwcListBean> jwcListBean = new ArrayList<>();

    public JwcListAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<JwcListBean> jwcListBean) {
        this.jwcListBean.addAll(jwcListBean);
    }

    public void clear() {
        jwcListBean.clear();
        notifyDataSetChanged();
    }


    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.activity_jwc_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        final JwcListBean bean = jwcListBean.get(position);

        viewHolder.title.setText(bean.getTitle());
        viewHolder.notice_kind.setText(bean.getKind());
        viewHolder.data.setText(bean.getTime());

        viewHolder.notice_onclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyUtils.openUrl(context, bean.getUrl());
            }
        });

    }

    @Override
    public long getItemId(int i) {
        return super.getItemId(i);
    }

    @Override
    public int getItemCount() {
        return jwcListBean.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout notice_onclick;
        TextView title;
        TextView notice_kind;
        TextView data;
        ViewHolder(View ll_class) {
            super(ll_class);
            title = ll_class.findViewById(R.id.notice_title);
            notice_kind = ll_class.findViewById(R.id.notice_kind);
            data = ll_class.findViewById(R.id.notice_time);
            notice_onclick = ll_class.findViewById(R.id.notice_onclick);
        }
    }

}