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

import com.blankj.utilcode.util.LogUtils;
import com.yangtzeu.R;
import com.yangtzeu.entity.GradeBean;
import com.yangtzeu.url.Url;
import com.yangtzeu.utils.MyUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


public class GradeAdapter extends RecyclerView.Adapter<GradeAdapter.ViewHolder> {
    private Context context;
    private List<GradeBean> gradeBeans=new ArrayList<>();

    public GradeAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<GradeBean> BigList) {
        gradeBeans = BigList;
    }

    public void clear() {
        gradeBeans.clear();
        notifyDataSetChanged();
    }

    public void sort(final boolean isUp) {
        Collections.sort(gradeBeans, new Comparator<GradeBean>() {
            @Override
            public int compare(GradeBean o1, GradeBean o2) {
                if (isUp)
                    return Double.valueOf(o1.getCourseZuiZhong()).compareTo(Double.valueOf(o2.getCourseZuiZhong()));
                else
                    return Double.valueOf(o2.getCourseZuiZhong()).compareTo(Double.valueOf(o1.getCourseZuiZhong()));
            }
        });
        notifyItemRangeChanged(0,getItemCount());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup view, int viewType) {
            return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.fragment_grade_part1_item, view, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, @SuppressLint("RecyclerView") final int i) {
        GradeBean gradeBean = gradeBeans.get(i);


        String courseYear = gradeBean.getCourseYear();
        String courseTerm = gradeBean.getCourseTerm();
        String courseCode = gradeBean.getCourseCode();
        String courseIndex = gradeBean.getCourseIndex();
        String courseName = gradeBean.getCourseName();
        String courseKind = gradeBean.getCourseKind();
        String courseScore = gradeBean.getCourseScore();
        String courseBuKao = gradeBean.getCourseBuKao();
        String courseZongPing = gradeBean.getCourseZongPing();
        String courseZuiZhong = gradeBean.getCourseZuiZhong();
        String coursePoint = gradeBean.getCoursePoint();

        viewHolder.st_name.setText(courseName);
        if (courseBuKao != null) {
            if (!courseBuKao.isEmpty()){
                viewHolder.st_grade.setText("课程成绩：" + courseBuKao + "/" + courseZuiZhong);}
            else{
                viewHolder.st_grade.setText("课程成绩：" +courseZuiZhong);
            }
        } else {
            viewHolder.st_grade.setText("课程成绩：" + courseZuiZhong);
        }

        viewHolder.st_jidian.setText("课程绩点：" + coursePoint);
        viewHolder.st_xuefen.setText("课程学分：" + courseScore);
        viewHolder.st_year.setText("课程学年：" + courseYear);
        viewHolder.st_kind.setText("课程类别：" + courseKind);

        final String details = "科目名称：" + courseName
                + "\n年份：" + courseYear
                + "\n学期：" + courseTerm
                + "\n补考成绩：" + courseBuKao
                + "\n总评成绩：" + courseZongPing
                + "\n最终成绩：" + courseZuiZhong
                + "\n课程学分：" + courseScore
                + "\n绩点：" + coursePoint
                + "\n课程种类：" + courseKind;

        viewHolder.onClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialog = new AlertDialog.Builder(context)
                        .setTitle("成绩详情")
                        .setMessage(details)
                        .setPositiveButton("知道了",null)
                        .setNegativeButton("分享成绩", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                MyUtils.shareText(context, "给你看看我的成绩噢" +
                                        "!\n\n" + details + "\n\n数据来自：" + Url.AppDownUrl);
                            }
                        })
                        .create();
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
            }
        });

        try {
            Double sss= Double.valueOf(courseZuiZhong);
            if (90 <= sss) {
                viewHolder.st_grade.setTextColor(context.getResources().getColor(R.color._90));
                viewHolder.st_view.setBackgroundColor(context.getResources().getColor(R.color._90));
                viewHolder.mCardView.setCardBackgroundColor(context.getResources().getColor(R.color.white));
            } else if (80 <= sss) {
                viewHolder.st_grade.setTextColor(context.getResources().getColor(R.color._80));
                viewHolder.st_view.setBackgroundColor(context.getResources().getColor(R.color._80));
                viewHolder.mCardView.setCardBackgroundColor(context.getResources().getColor(R.color.white));
            } else if (70 <= sss) {
                viewHolder.st_grade.setTextColor(context.getResources().getColor(R.color._70));
                viewHolder.st_view.setBackgroundColor(context.getResources().getColor(R.color._70));
                viewHolder.mCardView.setCardBackgroundColor(context.getResources().getColor(R.color.white));
            } else if (60 <= sss) {
                viewHolder.st_grade.setTextColor(context.getResources().getColor(R.color._60));
                viewHolder.st_view.setBackgroundColor(context.getResources().getColor(R.color._60));
                viewHolder.mCardView.setCardBackgroundColor(context.getResources().getColor(R.color.white));
            } else {
                viewHolder.st_grade.setTextColor(context.getResources().getColor(R.color._60_));
                viewHolder.st_view.setBackgroundColor(context.getResources().getColor(R.color._60_));
                viewHolder.mCardView.setCardBackgroundColor(context.getResources().getColor(R.color.white_d));
            }
        } catch (Exception e) {
            LogUtils.e("成绩颜色设置错误！");
        }
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return gradeBeans.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView st_name;
        TextView st_year;
        TextView st_kind;
        TextView st_xuefen;
        TextView st_grade;
        TextView st_jidian;
        View st_view;
        CardView mCardView;
        LinearLayout onClick;
        ViewHolder(View view) {
            super(view);
            mCardView = view.findViewById(R.id.mCardView);
            st_view = view.findViewById(R.id.view);
            st_name = view.findViewById(R.id.st_name);
            st_grade = view.findViewById(R.id.st_grade);
            st_jidian = view.findViewById(R.id.st_jidian);
            st_xuefen = view.findViewById(R.id.st_xuefen);
            st_year = view.findViewById(R.id.st_year);
            st_kind = view.findViewById(R.id.st_kind);
            onClick = view.findViewById(R.id.onClick);
        }
    }


}