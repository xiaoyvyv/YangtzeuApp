package com.yangtzeu.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.lib.subutil.ClipboardUtils;
import com.yangtzeu.R;
import com.yangtzeu.entity.WebBean;
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

public class WebListAdapter extends RecyclerView.Adapter<WebListAdapter.ViewHolder> {
    private Context context;
    private List<WebBean.WebListBean> webListBeans = new ArrayList<>();

    public WebListAdapter(Context context) {
        this.context = context;
    }

    public void setData(WebBean webBean) {
        this.webListBeans = webBean.getWebList();
    }

    public void clear() {
        webListBeans.clear();
        notifyDataSetChanged();
    }


    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.activity_web_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        WebBean.WebListBean itemBean = webListBeans.get(position);
        final String url = itemBean.getUrl();
        final String name = itemBean.getTitle();
        holder.title.setText(name);

        holder.url.setText(url);
        holder.url.getPaint().setAntiAlias(true);
        holder.url.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        holder.url.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardUtils.copyText(url);
                ToastUtils.showShort(R.string.copy_right);
            }
        });
        holder.mLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyUtils.openUrl(context, url);
            }
        });


    }

    @Override
    public long getItemId(int i) {
        return super.getItemId(i);
    }

    @Override
    public int getItemCount() {
        return webListBeans.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView url;
        LinearLayout mLinearLayout;

        ViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.title);
            url = view.findViewById(R.id.url);
            mLinearLayout = view.findViewById(R.id.mLinearLayout);
        }
    }

}