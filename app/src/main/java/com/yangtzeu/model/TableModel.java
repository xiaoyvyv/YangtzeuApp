package com.yangtzeu.model;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.google.android.material.tabs.TabLayout;
import com.yangtzeu.R;
import com.yangtzeu.database.DatabaseUtils;
import com.yangtzeu.database.MyOpenHelper;
import com.yangtzeu.entity.Course;
import com.yangtzeu.http.OkHttp;
import com.yangtzeu.http.OnResultStringListener;
import com.yangtzeu.model.imodel.ITableModel;
import com.yangtzeu.ui.activity.LoginActivity;
import com.yangtzeu.ui.view.TableView;
import com.yangtzeu.url.Url;
import com.yangtzeu.utils.MyUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import okhttp3.FormBody;
import okhttp3.Request;

public class TableModel implements ITableModel {
    private int week = 1;
    private String table_ids = "";


    @Override
    public void setWithTabLayout(final Activity activity, final TableView view) {
        String now_week_which = TimeUtils.getChineseWeek(TimeUtils.getNowMills());
        for (int i = 1; i < view.getWeekLayout().getChildCount(); i++) {
            TextView textView = (TextView) view.getWeekLayout().getChildAt(i);
            String text_week = textView.getText().toString().trim();
            if (StringUtils.equals(now_week_which, text_week)) {
                textView.setBackground(activity.getResources().getDrawable(R.mipmap.now_week));
            }
        }

        week = SPUtils.getInstance("user_info").getInt("table_week", 1);
        //若week为0，则表示假期，TabLayout默认选中周次1
        if (week==0) week = 1;

        final TabLayout tabLayout = view.getTabLayout();

        //添加标题
        for (int i = 0; i < 20; i++) {
            TabLayout.Tab tab = tabLayout.newTab();
            tab.setText("第" + (i + 1) + "周");
            tabLayout.addTab(tab);
        }

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                week = (tab.getPosition() + 1);
                SPUtils.getInstance("user_info").put("table_week", week);

                view.getCourse().clear();
                view.getTableFragmentAdapter().clear();

                loadTableDataStep1(activity, view);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                view.getTableFragmentAdapter().clear();

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                view.getRefreshLayout().autoRefresh();
            }
        });

        //选中当前的周次
        tabLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                int term_id = Integer.parseInt(SPUtils.getInstance("user_info").getString("term_id", Url.Default_Term));
                //若当前学期不等于默认学期则选中第一周，否则为本学期课表则选择当前周次
                if (term_id != Integer.parseInt(Url.Default_Term)) {
                    week = 1;
                }
                Objects.requireNonNull(tabLayout.getTabAt(week - 1)).select();
            }
        }, 100);


    }

    @Override
    public void loadTableDataStep1(final Activity activity, final TableView view) {
        OkHttp.do_Get(Url.Yangtzeu_Table_Ids, new OnResultStringListener() {
            @Override
            public void onResponse(String response) {
                try {
                    Document document = Jsoup.parse(response);
                    Elements scripts = document.select("script");
                    String script = scripts.get(scripts.size() - 1).toString();

                    String regex0 = "addInput(.*?);";
                    String week = MyUtils.getSubUtil(script, regex0).get(0);
                    String[] ids = week.split(",");
                    String temp = ids[2];
                    temp = temp.substring(temp.indexOf("\"") + 1, temp.lastIndexOf("\""));
                    table_ids = temp;
                    LogUtils.i("用户IDS", table_ids);

                    loadTableDataStep2(activity, view);
                } catch (Exception e) {
                    view.getRefreshLayout().finishRefresh();
                }
            }

            @Override
            public void onFailure(String error) {
                LogUtils.e("IDS获取失败：" + error);
                view.getRefreshLayout().finishRefresh();
            }
        });
    }

    @Override
    public void loadTableDataStep2(final Activity activity, final TableView view) {
        String term_id = SPUtils.getInstance("user_info").getString("term_id", Url.Default_Term);
        FormBody formBody = new FormBody.Builder()
                .add("ignoreHead", "1")
                .add("setting.kind", "std")
                .add("startWeek", String.valueOf(week))
                .add("project.id", "1")
                .add("semester.id", term_id)
                .add("ids", table_ids)
                .build();

        Request request = new Request.Builder()
                .url(Url.Yangtzeu_Table)
                .post(formBody)
                .build();

        OkHttp.do_Post(request, new OnResultStringListener() {
            @Override
            public void onResponse(String response) {
                view.getRefreshLayout().finishRefresh();
                view.getCourse().clear();
                view.getTableFragmentAdapter().clear();

                try {
                    Document document = Jsoup.parse(response);
                    Elements scripts = document.select("script");

                    String Class_Script = scripts.get(scripts.size() - 2).toString();

                    //利用 【activity;】截取课程为一个集合
                    String regex = "activity;";
                    String[] s = Class_Script.split(regex);

                    if (s.length < 2) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                        builder.setTitle("提示");
                        builder.setMessage("未获取到课表数据，可能原因：\n\n1.学期选择错误\n2.周次超出课程安排\n3.数据请求出错\n4.没有课程安排");
                        builder.setPositiveButton("重新获取", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                view.getRefreshLayout().autoRefresh();
                            }
                        });
                        builder.setNegativeButton("恢复默认学期", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SPUtils.getInstance("user_info").put("term_id", Url.Default_Term);
                                view.getRefreshLayout().autoRefresh();
                            }
                        });
                        if (week != 1)
                            builder.setNeutralButton("上一周", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Objects.requireNonNull(view.getTabLayout().getTabAt(week - 2)).select();
                                }
                            });

                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }

                    for (int i = 0; i < s.length - 1; i++) {
                        Course course = new Course();
                        String[] weeks = regexWeekAndSection(s[i]);
                        course.setWeek(weeks[0]);
                        course.setSection(weeks[1]);
                        //LogUtils.e(weeks[0],weeks[1]);

                        String[] tasks = regexTask(s[i]);
                        course.setMid(tasks[0]);
                        course.setName(tasks[1]);
                        course.setRoom_id(tasks[2]);
                        course.setRoom(tasks[3]);
                        course.setAll_week(tasks[4]);
                        //LogUtils.e(tasks[0],tasks[1],tasks[2],tasks[3],tasks[4]);

                        String[] teacher = getTeacher(s[i]);
                        course.setTeacher(teacher[1]);
                        //LogUtils.e(teacher[1]);

                        view.getCourse().add(course);
                    }

                    ShowCourse(activity, view);
                } catch (Exception e) {
                    LogUtils.e(e);
                    SPUtils.getInstance("user_info").put("online", false);
                    ActivityUtils.finishAllActivities();
                    MyUtils.startActivity(LoginActivity.class);
                }
            }

            @Override
            public void onFailure(String error) {
                view.getRefreshLayout().finishRefresh();
                LogUtils.e(error);
            }
        });
    }

    @Override
    public void setTableBackground(final Activity activity, TableView view) {
        final ImageView table_bg = view.getTableBackgroundView();
        String url_bg = SPUtils.getInstance("app_info").getString("table_bg", Url.Yangtzeu_Table_Background);
        if (URLUtil.isNetworkUrl(url_bg)) {
            table_bg.setScaleType(ImageView.ScaleType.FIT_XY);
            MyUtils.loadImage(activity, table_bg, url_bg);
        } else {
            table_bg.setScaleType(ImageView.ScaleType.CENTER_CROP);
            MyUtils.loadImageNoCache(activity, table_bg, new File(url_bg));
        }
    }

    private void ShowCourse(Activity activity, TableView view) {
        view.getTableFragmentAdapter().clear();

        MyOpenHelper helper = DatabaseUtils.getHelper("table.db");
        if (helper.queryAll(Course.class) != null) {
            helper.clear(Course.class);
        }
        for (Course course : view.getCourse()) {
            view.getTableFragmentAdapter().addCourse(course);
            if (week == SPUtils.getInstance("user_info").getInt("table_week")){
                helper.save(course);
            }
        }

        view.getTableFragmentAdapter().notifyItemRangeChanged(0, 42);
    }


    private String[] regexWeekAndSection(String s) {
        String[] week_section = new String[2];
        /*
         *  匹配   =0*unitCount+4
         */
        String regex0 = "index(.*?);";
        List<String> week = MyUtils.getSubUtil(s, regex0);
        StringBuilder Str = new StringBuilder();
        for (String w : week) {
            if (w.contains("*"))
                Str.append(w);
        }
        try {
            week_section[0] = Str.substring(Str.indexOf("=") + 1, Str.lastIndexOf("*"));
            week_section[1] = Str.substring(Str.indexOf("+") + 1, Str.length());
        } catch (Exception e) {
            week_section[0] = "-1";
            week_section[1] = "-1";
        }
        return week_section;
    }

    private String[] getTeacher(String s) {
        String[] teacher = new String[2];
        String regex0 = "teachers(.*?);";
        String teacher_id;
        String teacher_name;
        try {
            String str1 = MyUtils.getSubUtil(s, regex0).get(0);
            String str2 = str1.substring(str1.indexOf(":"), str1.lastIndexOf(","));

            teacher_id = str2.substring(0, str2.lastIndexOf(","));
            teacher_name = str2.substring(str2.indexOf("\"") + 1, str2.lastIndexOf("\""));

            teacher[0] = teacher_id;
            teacher[1] = teacher_name;
        } catch (Exception e) {
            teacher[0] = "0";
            teacher[1] = "同上";
        }
        return teacher;
    }

    private String[] tempTask;

    private String[] regexTask(String s) {
        String[] tasks = new String[5];
        /*
         *  匹配  (actTeacherId.join(','), actTeacherName.join(','), "104708(393579)", "中国象棋(393579)", "221", "东12-502c", "00011111111111000000000000000000000000000000000000000", null, null, assistantName, "", "")
         */
        String regex0 = "TaskActivity(.*?);";
        List<String> list = MyUtils.getSubUtil(s, regex0);
        StringBuilder Str = new StringBuilder();
        for (String w : list) {
            Str.append(w);
        }
        String[] params = Str.toString().split(",");
        if (params.length > 2) {
            tempTask = new String[params.length];
            tempTask = params;
            LogUtils.i("课表数据大小：" + params.length, Arrays.deepToString(params));
        } else {
            params = tempTask;
            LogUtils.i("重复课表数据大小：" + params.length, Arrays.deepToString(params));
        }
        for (int i = 0; i < params.length; i++) {
            String now = params[i];
            switch (i) {
                case 4:
                    now = now.substring(now.indexOf("(") + 1, now.lastIndexOf(")"));
                    tasks[0] = now;
                    break;
                case 5:
                    tasks[1] = now;
                    break;
                case 6:
                    tasks[2] = now;
                    break;
                case 7:
                    tasks[3] = now;
                    break;
                case 8:
                    tasks[4] = now;
                    break;
            }
        }
        return tasks;
    }
}