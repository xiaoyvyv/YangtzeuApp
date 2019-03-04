package com.yangtzeu.ui.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.android.material.textfield.TextInputEditText;
import com.yangtzeu.R;
import com.yangtzeu.entity.Course;
import com.yangtzeu.url.Url;
import com.yangtzeu.utils.MyUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by 2016 on 2017/12/4.
 */

public class TableFragmentAdapter extends RecyclerView.Adapter<TableFragmentAdapter.ViewHolder> {
    private Context context;
    private Activity activity;
    private String alpha;
    @SuppressLint("UseSparseArrays")
    private HashMap<Integer, Course[]> booleans = new HashMap<>();
    private HashMap<String, String> colorHash = new HashMap<>();
    private String[] tempColor;

    public TableFragmentAdapter(Context context) {
        this.context = context;
        activity = (Activity) context;
        for (int i = 0; i < getItemCount(); i++) {
            Course courses[] = new Course[2];
            booleans.put(i, courses);
        }

        initColor();
    }

    private void initColor() {
        colorHash.clear();
        alpha = SPUtils.getInstance("app_info").getString("table_alpha", "90");
        tempColor = new String[]{"#" + alpha + "ff80ab", "#" + alpha + "4CAF50", "#" + alpha + "FF4081",
                "#" + alpha + "3f51b5", "#" + alpha + "009688", "#" + alpha + "f57c00", "#" + alpha + "673ab7",
                "#" + alpha + "2196f3", "#" + alpha + "795548", "#" + alpha + "607d8b", "#" + alpha + "515151", "#" + alpha + "D9cc7E"};
    }

    public void addCourse(Course course) {
        int week = -1;
        int section = -1;
        Course courses[] = new Course[2];
        if (!course.getWeek().isEmpty()) {
            week = Integer.parseInt(course.getWeek());
        }
        if (!course.getSection().isEmpty()) {
            section = Integer.parseInt(course.getSection());
        }

        if (week == -1 || section == -1) {
            return;
        }

        int where = GetPositionByWhichAndSection(week + 1, section + 1);
        if (Objects.requireNonNull(booleans.get(where))[0] == null) {
            courses[0] = course;
            booleans.put(where, courses);
        } else {
            courses[0] = Objects.requireNonNull(booleans.get(where))[0];
            courses[1] = course;
            booleans.put(where, courses);
        }
    }

    public void clear() {
        for (int i = 0; i < getItemCount(); i++) {
            Course courses[] = new Course[2];
            booleans.put(i, courses);
        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.fragment_table_item, viewGroup, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, @SuppressLint("RecyclerView") final int i) {
        if (i == 0) {
            color_index = 0;
            initColor();
        }

        Course course = Objects.requireNonNull(booleans.get(i))[0];
        final Course course_more = Objects.requireNonNull(booleans.get(i))[1];

        if (course == null) {
            final int week = SPUtils.getInstance("user_info").getInt("table_week", 10);
            Map<String, ?> list = SPUtils.getInstance("my_set_class").getAll();
            String mSetClass = (String) list.get(week + "_" + String.valueOf(i));

            if (mSetClass != null) {
                viewHolder.ClassCardView.setCardBackgroundColor(Color.parseColor("#" + alpha + "ff80ab"));
                viewHolder.ClassCardView.setVisibility(View.VISIBLE);
                viewHolder.mClass.setText("自定义课表\n\n" + mSetClass);
                viewHolder.onclick.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog dialog = new AlertDialog.Builder(context)
                                .setTitle("提示")
                                .setMessage("是否删除自定义内容")
                                .setPositiveButton(R.string.done, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        int week = SPUtils.getInstance("user_info").getInt("table_week", 10);
                                        SPUtils.getInstance("my_set_class").remove(week + "_" + String.valueOf(i));
                                        notifyDataSetChanged();
                                    }
                                })
                                .setNegativeButton(R.string.clear, null)
                                .create();
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.show();
                    }
                });
            } else {
                final int[] where = GetWhichByAndSectionPosition(i);
                viewHolder.ClassCardView.setVisibility(View.INVISIBLE);
                viewHolder.onclick.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (i >= 42) {
                            ToastUtils.showShort("不支持自定义午间晚间课表");
                            return;
                        }

                        @SuppressLint("InflateParams")
                        View view = activity.getLayoutInflater().inflate(R.layout.view_table_dialog, null);
                        final TextInputEditText start = view.findViewById(R.id.start);
                        final TextInputEditText end = view.findViewById(R.id.end);
                        final TextInputEditText text = view.findViewById(R.id.text);
                        final TextView trip = view.findViewById(R.id.trip);
                        trip.setText("第" + week + "周-星期" + where[1] + "-第" + where[0] + "节");

                        AlertDialog dialog = new AlertDialog.Builder(context)
                                .setView(view)
                                .setPositiveButton(R.string.done, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        String str_start = Objects.requireNonNull(start.getText()).toString().trim();
                                        String str_end = Objects.requireNonNull(end.getText()).toString().trim();
                                        String str_text = Objects.requireNonNull(text.getText()).toString();
                                        if (!str_start.isEmpty() && !str_end.isEmpty() && !str_text.isEmpty()) {
                                            int start = Integer.parseInt(str_start);
                                            int end = Integer.parseInt(str_end);

                                            if (start >= end) {
                                                ToastUtils.showShort("开始周大于大于结束周，请重新输入");
                                                return;
                                            }
                                            for (int j = start; j < end + 1; j++) {
                                                SPUtils.getInstance("my_set_class").put(j + "_" + String.valueOf(i), str_text);
                                            }
                                            notifyDataSetChanged();
                                        } else
                                            ToastUtils.showShort(R.string.please_input);
                                    }
                                })
                                .setNegativeButton(R.string.clear, null)
                                .create();
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.show();
                    }
                });
            }
            return;
        }

        viewHolder.ClassCardView.setVisibility(View.VISIBLE);
        String name = course.getName();
        name = name.replace("\"", "");
        String room = course.getRoom();
        room = room.replace("\"", "");

        final String id = course.getMid();
        final String section = course.getSection();
        final String teacher = course.getTeacher();
        final String week = course.getWeek();

        viewHolder.mClass.setText(name + "\n\n" + teacher + "\n\n" + room);


        final String finalName = name;
        final String finalRoom = room;

        //不透明时设置阴影
        if (alpha.equals("ff")) {
            viewHolder.ClassCardView.setCardElevation(ConvertUtils.dp2px(1));
        } else {
            viewHolder.ClassCardView.setCardElevation(ConvertUtils.dp2px(0));
        }

        if (course_more != null) {
            viewHolder.ClassCardView.setCardBackgroundColor(Color.parseColor("#" + alpha + "ff0000"));

            String name_more = course_more.getName();
            name_more = name_more.replace("\"", "");

            final String teacher_more = course_more.getTeacher();
            final String room_more = course_more.getRoom();
            final String finalName_more = name_more;

            viewHolder.mClass.setText(finalName + "\n\n" + finalName_more + "\n\n" + room);
            viewHolder.onclick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final String details = "存在冲突课程\n\n课程一名称：" + finalName
                            + "\n课程二名称：" + finalName_more
                            + "\n课程一老师：" + teacher
                            + "\n课程二老师：" + teacher_more
                            + "\n课程一教室：" + finalRoom
                            + "\n课程二教室：" + room_more
                            + "\n课程周次：星期" + (Integer.parseInt(week) + 1)
                            + "\n课程节次：" + "第" + (Integer.parseInt(section) + 1) + "大节";

                    AlertDialog dialog = new AlertDialog.Builder(context)
                            .setTitle("课程详情")
                            .setMessage(details)
                            .setPositiveButton("知道了", null)
                            .setNegativeButton("分享课程", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    MyUtils.shareText(context, "给你看看我的课程噢!\n\n" + details + "\n\n数据来自：" + Url.AppDownUrl);
                                }
                            })
                            .create();
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();
                }
            });
        } else {
            String colorStr = getColor(id);
            viewHolder.ClassCardView.setCardBackgroundColor(Color.parseColor(colorStr));
            viewHolder.onclick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                     String details = "课程名称：" + finalName
                            + "\n课程周次：星期" + (Integer.parseInt(week) + 1)
                            + "\n课程节次：" + "第" + (Integer.parseInt(section) + 1) + "大节"
                            + "\n课程老师：" + teacher
                            + "\n课程教室：" + finalRoom;

                    if (i >= 42) {
                        details = details + "\n\n备注：此课程时间为午间或晚间课";
                    }

                    final String finalDetails = details;
                    AlertDialog dialog = new AlertDialog.Builder(context)
                            .setTitle("课程详情")
                            .setMessage(finalDetails)
                            .setPositiveButton("知道了", null)
                            .setNegativeButton("分享课程", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    MyUtils.shareText(context, "给你看看我的课程噢!\n\n" + finalDetails + "\n\n数据来自：" + Url.AppDownUrl);
                                }
                            })
                            .create();
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();
                }
            });
        }


    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return 56;
    }


    private int color_index = 0;
    private String getColor(String id) {
        String color = colorHash.get(id);
        if (color == null) {
            if (tempColor.length > color_index) {
                color = tempColor[color_index];
                color_index++;
            } else {
                Random rand = new Random();
                String red = MyUtils.formattingH(rand.nextInt(255));
                String green = MyUtils.formattingH(rand.nextInt(255));
                String blue = MyUtils.formattingH(rand.nextInt(255));
                color = "#" + alpha + red + green + blue;
            }
            colorHash.put(id, color);
        }
        return color;
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        CardView ClassCardView;
        LinearLayout onclick;
        TextView mClass;

        ViewHolder(View view) {
            super(view);
            ClassCardView = view.findViewById(R.id.ClassCardView);
            onclick = view.findViewById(R.id.onclick);
            mClass = view.findViewById(R.id.mClass);
        }
    }

    private int GetPositionByWhichAndSection(int Week, int Section) {
        return (Section * 7 - 1) - (7 - Week);
    }

    private static int[] GetWhichByAndSectionPosition(int position) {
        int[] sec_which = new int[2];
        sec_which[0] = position / 7 + 1;
        sec_which[1] = position % 7 + 1;
        return sec_which;
    }
}
