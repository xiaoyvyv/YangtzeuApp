package com.yangtzeu.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
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
    private int spanCount;

    public ManyAdapter(Context context) {
        this.context = context;
    }

    public void setSpanCount(int spanCount) {
        this.spanCount = spanCount;
    }

    public void setData(ManyBean bean) {
        this.appBeans = bean.getData();
    }

    public void clear() {
        appBeans.clear();
        notifyDataSetChanged();
    }


    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.fragment_many_item, parent, false));
    }

    @SuppressLint("CheckResult")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        int dp30 = ConvertUtils.dp2px(30);
        int dp50 = ConvertUtils.dp2px(50);
        int screenWidth = ScreenUtils.getScreenWidth();
        int item_screen = (int) (screenWidth / (spanCount * 1.0f));
        ViewGroup.LayoutParams params = holder.itemView.getLayoutParams();
        switch (position % 2) {
            case 0:
                params = new ViewGroup.LayoutParams(params.width, item_screen + dp30);
                break;
            case 1:
                params = new ViewGroup.LayoutParams(params.width , item_screen + dp50);
                break;
        }
        holder.itemView.setLayoutParams(params);


        ManyBean.DataBean itemBean = appBeans.get(position);
        final String url = itemBean.getUrl();
        final String title = itemBean.getTitle();
        final String icon = itemBean.getIcon();
        final String image = itemBean.getImage();

        RequestOptions option = new RequestOptions();
        option.error(R.mipmap.holder);
        option.placeholder(R.mipmap.holder);
        Glide.with(context).load(image).apply(option).into(holder.image);
        holder.icon.setText(icon);
        holder.title.setText(title);
        holder.title.setTextSize(itemBean.getFont());
        holder.OnClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (url.equals("http://39.104.135.75/WebForm.aspx")) {
                    ToastUtils.showLong("体测成绩查询的默认密码为：123456");
                }
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
        ImageView image;
        TextView title;
        TextView icon;
        LinearLayout OnClick;

        ViewHolder(View view) {
            super(view);
            icon = view.findViewById(R.id.icon);
            image = view.findViewById(R.id.image);
            title = view.findViewById(R.id.title);
            OnClick = view.findViewById(R.id.OnClick);
        }
    }


}