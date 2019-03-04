package com.yangtzeu.ui.adapter;

/**
 * Created by Administrator on 2018/1/30.
 */

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yangtzeu.R;
import com.yangtzeu.entity.PointBean;
import com.yangtzeu.url.Url;
import com.yangtzeu.utils.MyUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class PointAdapter extends RecyclerView.Adapter<PointAdapter.ViewHolder> {
    private Context context;
    private List<PointBean> pointBeans=new ArrayList<>();

    public PointAdapter(Context context) {
        this.context = context;
    }
    public void setData(List<PointBean> BigList) {
        pointBeans = BigList;
    }
    public void clear() {
        pointBeans.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup view, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.fragment_grade_part3_item, view, false));

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, @SuppressLint("RecyclerView") final int i) {
        PointBean gradeBean = pointBeans.get(i);

        String courseYear = gradeBean.getCourseYear();
        String courseTerm = gradeBean.getCourseTerm();
        String courseScore = gradeBean.getCourseScore();
        String courseNumber = gradeBean.getCourseNumber();
        String coursePoint = gradeBean.getCoursePoint();

        viewHolder.st_year.setText("课程学年：" + courseYear);
        viewHolder.st_term.setText("课程学期：" + courseTerm);
        viewHolder.st_number.setText("课程门数：" + courseNumber);
        viewHolder.st_score.setText("课程学分：" + courseScore);
        viewHolder.st_point.setText("课程绩点：" + coursePoint);

        final String details = "学年度：" + courseYear
                + "\n学期：" + courseTerm
                + "\n门数：" + courseNumber
                + "\n总学分：" +courseScore
                + "\n平均绩点：" + coursePoint;


        viewHolder.onClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialog = new AlertDialog.Builder(context)
                        .setTitle("绩点详情")
                        .setMessage(details)
                        .setPositiveButton("知道了",null)
                        .setNegativeButton("分享绩点", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                MyUtils.shareText(context, "给你看看我的绩点噢" +
                                        "!\n\n" + details + "\n\n数据来自：" + Url.AppDownUrl);
                            }
                        })
                        .create();
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
            }
        });
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return pointBeans.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView st_year;
        TextView st_term;
        TextView st_number;
        TextView st_score;
        TextView st_point;

        LinearLayout onClick;
        ViewHolder(View view) {
            super(view);
            st_year = view.findViewById(R.id.st_year);
            st_term = view.findViewById(R.id.set_term);
            st_number = view.findViewById(R.id.st_number);
            st_score = view.findViewById(R.id.st_score);
            st_point = view.findViewById(R.id.st_point);
            onClick = view.findViewById(R.id.onClick);
        }
    }
}