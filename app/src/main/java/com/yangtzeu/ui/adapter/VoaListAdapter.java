package com.yangtzeu.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yangtzeu.R;
import com.yangtzeu.ui.activity.VoaDetailsActivity;
import com.yangtzeu.url.Url;
import com.yangtzeu.utils.MyUtils;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

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

public class VoaListAdapter extends RecyclerView.Adapter<VoaListAdapter.ViewHolder> {
    private Context context;
    private List<Element> appBeans = new ArrayList<>();

    public VoaListAdapter(Context context) {
        this.context = context;
    }

    public void setData(Elements elements) {
        this.appBeans.addAll(elements);
    }

    public void clear() {
        appBeans.clear();
        notifyDataSetChanged();
    }


    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.activity_voa_item, parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Element element = appBeans.get(position);
        final String url = Url.Yangtzeu_Voa_Home + element.attr("href");

        holder.name.setText(element.text());
        holder.mLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, VoaDetailsActivity.class);
                intent.putExtra("title", element.text());
                intent.putExtra("from_url", url);
                MyUtils.startActivity(intent);
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
        TextView name;
        LinearLayout mLinearLayout;
        ViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.title);
            mLinearLayout = view.findViewById(R.id.onclick);
        }
    }

}