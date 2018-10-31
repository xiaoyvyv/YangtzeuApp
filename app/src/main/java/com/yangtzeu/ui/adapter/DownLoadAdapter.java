package com.yangtzeu.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.yangtzeu.R;
import com.yangtzeu.entity.FileBean;
import com.yangtzeu.utils.MyUtils;
import com.yangtzeu.utils.ShareUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;


/**
 * Created by Administrator on 2018/4/12.
 *
 * @author 王怀玉
 * @explain MoreAdapter
 */

public class DownLoadAdapter extends RecyclerView.Adapter<DownLoadAdapter.ViewHolder> {
    private Context context;
    private List<FileBean> fileBeans = new ArrayList<>();

    public DownLoadAdapter(Context context) {
        this.context = context;
    }

    public void loadData() {
        String filePath = MyUtils.rootPath() + SPUtils.getInstance("app_info").getString("save_path", "A_Tool/Download/");
        List<File> files = new ArrayList<>();
        MyUtils.listFilesInDir(files, filePath);

        Collections.sort(files, new MyUtils.FileComparator2());
        fileBeans.clear();
        notifyDataSetChanged();

        for (File file : files) {
            FileBean fileBean = new FileBean();
            String name = FileUtils.getFileName(file);
            String path = file.getAbsolutePath();
            long time = file.lastModified();
            String size = FileUtils.getFileSize(file);
            String type = FileUtils.getFileExtension(file);

            fileBean.setName(name);
            fileBean.setPath(path);
            fileBean.setSize(size);
            fileBean.setType(type);
            fileBean.setTime(TimeUtils.millis2String(time));

            fileBeans.add(fileBean);
        }

        if (ObjectUtils.isEmpty(fileBeans)) {
            ToastUtils.showShort(R.string.no_data);
            return;
        }
        notifyItemRangeChanged(0, getItemCount());
    }


    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.download_activity_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FileBean fileBean = fileBeans.get(position);
        final String path = fileBean.getPath();
        final String name = fileBean.getName();
        final String size = fileBean.getSize();
        final String type = fileBean.getType();
        final String time = fileBean.getTime();


        holder.file_name.setText(name);
        holder.file_size.setText(size);
        holder.file_time.setText(time);

        switch (type) {
            case "png":
            case "jpg":
            case "gif":
                Glide.with(context).load(R.mipmap.jpg).into(holder.file_image);
                break;
            case "mp4":
                Glide.with(context).load(R.mipmap.video).into(holder.file_image);
                break;
            case "mp3":
                Glide.with(context).load(R.mipmap.mp3).into(holder.file_image);
                break;
            case "apk":
                Glide.with(context).load(R.mipmap.apk).into(holder.file_image);
                break;
            case "zip":
            case "rar":
                Glide.with(context).load(R.mipmap.zip).into(holder.file_image);
                break;
            case "txt":
            case "doc":
            case "docx":
            case "ppt":
            case "xls":
            case "xlsx":
                Glide.with(context).load(R.mipmap.txt).into(holder.file_image);
                break;
            default:
                Glide.with(context).load(R.mipmap.ic_launcher).into(holder.file_image);
                break;
        }

        holder.mLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final File file = new File(path);
                LogUtils.i(path);
                AlertDialog dialog = new AlertDialog.Builder(context)
                        .setTitle(file.getName())
                        .setMessage("请选择操作")
                        .setPositiveButton("打开", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                MyUtils.openFile(context, path);
                            }
                        })
                        .setNegativeButton("分享", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ShareUtils.shareFile(context, file);
                            }
                        })
                        .setNeutralButton("删除", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (file.delete()) {
                                    loadData();
                                    ToastUtils.showLong("删除成功");
                                } else {
                                    ToastUtils.showLong("删除失败");
                                }
                            }
                        })
                        .create();
                dialog.show();
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
        ImageView file_image;
        TextView file_size;
        TextView file_name;
        TextView file_time;
        LinearLayout mLinearLayout;

        ViewHolder(View view) {
            super(view);
            file_image = view.findViewById(R.id.file_image);
            file_size = view.findViewById(R.id.file_size);
            file_name = view.findViewById(R.id.file_name);
            file_time = view.findViewById(R.id.file_time);
            mLinearLayout = view.findViewById(R.id.mLinearLayout);
        }
    }

}