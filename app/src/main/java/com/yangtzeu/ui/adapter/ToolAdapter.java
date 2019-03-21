package com.yangtzeu.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.blankj.utilcode.util.FileUtils;
import com.bumptech.glide.Glide;
import com.yangtzeu.R;
import com.yangtzeu.entity.FileBean;
import com.yangtzeu.utils.MyUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


/**
 * Created by Administrator on 2018/4/12.
 *
 * @author 王怀玉
 * @explain MoreAdapter
 */

public class ToolAdapter extends RecyclerView.Adapter<ToolAdapter.ViewHolder> {
    private Context context;
    private List<FileBean> fileBeans = new ArrayList<>();

    public ToolAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<FileBean> fileBeans) {
        this.fileBeans = fileBeans;
    }

    public void clear() {
        fileBeans.clear();
        notifyDataSetChanged();
    }


    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.activity_tool_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        FileBean itemBean = fileBeans.get(position);

        final String name = itemBean.getName();
        final String path = itemBean.getPath();
        final String size = itemBean.getSize();
        final String time = itemBean.getTime();
        final String type = itemBean.getType();
        final File file = new File(path);

        Glide.with(context).load(new File(path)).into(holder.image);

        holder.time.setText(time);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyUtils.openImage(context,path);
            }
        });
        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                PopupMenu menu = new PopupMenu(context, holder.itemView);
                menu.getMenu().add("移除").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        notifyItemRemoved(position);
                        fileBeans.remove(position);
                        notifyItemRangeChanged(position, getItemCount());
                        FileUtils.delete(file);
                        return true;
                    }
                });
                menu.getMenu().add("移除全部").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        fileBeans.clear();
                        notifyDataSetChanged();
                        FileUtils.deleteAllInDir(file.getParentFile());
                        return true;
                    }
                });
                menu.show();

                return true;
            }
        });
    }

    @Override
    public long getItemId(int i) {
        return super.getItemId(i);
    }

    @Override
    public int getItemCount() {
        return fileBeans.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView time;
        CardView cardView;

        ViewHolder(View view) {
            super(view);
            image = view.findViewById(R.id.image);
            time = view.findViewById(R.id.time);
            cardView = view.findViewById(R.id.cardView);
        }
    }

}