package com.yangtzeu.ui.adapter;

/**
 * Created by Administrator on 2018/1/30.
 */

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yangtzeu.R;
import com.yangtzeu.entity.NewsBean;
import com.yangtzeu.ui.activity.NewsDetailsActivity;
import com.yangtzeu.url.Url;
import com.yangtzeu.utils.MyUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {
    private Context context;
    private List<NewsBean> newsBeans = new ArrayList<>();
    public NewsAdapter(Context context) {
        this.context = context;
    }

    public void setDate(List<NewsBean> newsBeans) {

        this.newsBeans = newsBeans;
    }

    public void clearData() {
        this.newsBeans.clear();
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup view, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.fragment_home_part1_item, view, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, @SuppressLint("RecyclerView") final int i) {
        NewsBean newsBean = newsBeans.get(i);

        viewHolder.Title.setText(newsBean.getTilte());
        viewHolder.Kind.setText(newsBean.getKind());
        viewHolder.Time.setText(newsBean.getTime());
        final String url = newsBean.getUrl();
        viewHolder.mLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (url.contains(Url.Yangtzeu_JWC) || url.contains(Url.Yangtzeu_News)) {
                    Intent intent = new Intent(context, NewsDetailsActivity.class);
                    intent.putExtra("from_url", url);
                    MyUtils.startActivity(intent);
                } else {
                    MyUtils.openUrl(context, url);
                }
            }
        });
        viewHolder.mLinearLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                MyUtils.openBrowser(context,url);
                return true;
            }
        });

    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return newsBeans.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView Title;
        TextView Kind;
        TextView Time;
        LinearLayout mLinearLayout;

        ViewHolder(View itemView) {
            super(itemView);
            Title = itemView.findViewById(R.id.notice_title);
            Kind = itemView.findViewById(R.id.notice_kind);
            Time = itemView.findViewById(R.id.notice_time);
            mLinearLayout = itemView.findViewById(R.id.notice_onclick);
        }
    }
}