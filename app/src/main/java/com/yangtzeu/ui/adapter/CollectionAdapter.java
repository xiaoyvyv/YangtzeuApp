package com.yangtzeu.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yangtzeu.R;
import com.yangtzeu.database.DatabaseUtils;
import com.yangtzeu.entity.CollectionBean;
import com.yangtzeu.listener.ItemTouchHelperAdapter;
import com.yangtzeu.utils.MyUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


/**
 * Created by Administrator on 2018/4/12.
 *
 * @author 王怀玉
 * @explain MoreAdapter
 */

public class CollectionAdapter extends RecyclerView.Adapter<CollectionAdapter.ViewHolder> implements ItemTouchHelperAdapter {
    private Context context;
    private List<CollectionBean> beans = new ArrayList<>();

    public CollectionAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<CollectionBean> beans) {
        this.beans = beans;
    }

    public void clear() {
        beans.clear();
        notifyItemRangeChanged(0, getItemCount());
    }


    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.activity_collection_item, parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CollectionBean itemBean = beans.get(position);
        final String url =itemBean.getUrl();
        final String time =itemBean.getTime();
        final String title =itemBean.getTitle();
        final long id =itemBean.getId();

        holder.url.setText(url);
        holder.time.setText(time);
        holder.title.setText(title);

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
        return beans.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        //交换位置
        Collections.swap(beans,fromPosition,toPosition);
        notifyItemMoved(fromPosition,toPosition);
    }

    @Override
    public void onItemMiss(int position) {
        CollectionBean collectionBean = beans.get(position);
        DatabaseUtils.getHelper(context,"collection.db").delete(collectionBean);
        //移除数据
        beans.remove(position);
        notifyItemRemoved(position);
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView time ;
        TextView url ;
        LinearLayout mLinearLayout;
        ViewHolder(View view) {
            super(view);
            url = view.findViewById(R.id.url);
            time = view.findViewById(R.id.time);
            title = view.findViewById(R.id.title);
            mLinearLayout = view.findViewById(R.id.mLinearLayout);
        }
    }

}