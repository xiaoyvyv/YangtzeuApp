package com.yangtzeu.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.CacheDiskUtils;
import com.blankj.utilcode.util.EncryptUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.lib.mob.im.IMManager;
import com.lib.subutil.GsonUtils;
import com.yangtzeu.R;
import com.yangtzeu.database.DatabaseUtils;
import com.yangtzeu.database.MyOpenHelper;
import com.yangtzeu.entity.AlertBean;
import com.yangtzeu.entity.BanBean;
import com.yangtzeu.entity.Course;
import com.yangtzeu.entity.MessageBean;
import com.yangtzeu.entity.OnLineBean;
import com.yangtzeu.entity.TripBean;
import com.yangtzeu.entity.UpDateBean;
import com.yangtzeu.entity.UserBean;
import com.yangtzeu.entity.YzBannerBean;
import com.yangtzeu.http.OkHttp;
import com.yangtzeu.http.OnResultStringListener;
import com.yangtzeu.listener.OnClassListener;
import com.yangtzeu.listener.OnResultListener;
import com.yangtzeu.ui.activity.LoginActivity;
import com.yangtzeu.ui.activity.TripActivity;
import com.yangtzeu.url.Url;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class YangtzeuUtils {

    //长江大学官网Banner获取
    public static void getBanner(final Context context) {
        OkHttp.do_Get(Url.Yangtzeu_Url, new OnResultStringListener() {
            @Override
            public void onResponse(String response) {
                List<YzBannerBean> list = new ArrayList<>();
                Document doc = Jsoup.parse(response);
                Elements elements = doc.select("div.iban div.bd ul li");
                for (int i = 0; i < elements.size(); i++) {
                    YzBannerBean yzBannerBean = new YzBannerBean();
                    String image_style = elements.get(i).attr("style");
                    String image_url = Url.Yangtzeu_Url + image_style.substring(image_style.indexOf("(") + 1, image_style.indexOf(")"));
                    String context_url = elements.get(i).select("li a").attr("href");
                    yzBannerBean.setImage(image_url);
                    yzBannerBean.setUrl(context_url);
                    yzBannerBean.setTitle(context.getString(R.string.yangtzeu_new_message));
                    list.add(yzBannerBean);
                }
                LogUtils.i("首页Banner数量：" + elements.size());

                MyOpenHelper helper = DatabaseUtils.getHelper(context, "banner.db");
                if (helper.queryAll(YzBannerBean.class) != null) {
                    helper.clear(YzBannerBean.class);
                }
                helper.saveAll(list);
            }

            @Override
            public void onFailure(String error) {
                LogUtils.e(error);
            }
        });
    }


    //App导航消息获取
    public static void getTripInfo(final Context context, final boolean isForce) {
        OkHttp.do_Get(Url.Yangtzeu_AppTripInfo, new OnResultStringListener() {
            @Override
            public void onResponse(String response) {
                try {
                    Gson gson = new Gson();
                    TripBean tripBean = gson.fromJson(response, TripBean.class);
                    String oldVersion = SPUtils.getInstance("app_info").getString("trip_version", "0");
                    final String newVersion = tripBean.getVersion();
                    if (oldVersion.equals(newVersion) && !isForce) {
                        return;
                    }
                    SPUtils.getInstance("app_info").put("trip_version", newVersion);
                    Intent intent = new Intent(context, TripActivity.class);
                    intent.putExtra("tripBean", tripBean);
                    MyUtils.startActivity(intent);
                } catch (Exception e) {
                    ToastUtils.showShort("初始化失败");
                }
            }

            @Override
            public void onFailure(String error) {
                LogUtils.e(error);
            }
        });

    }

    //AppAlert通知获取
    public static void getAlertNotice(final Context context) {
        OkHttp.do_Get(Url.Yangtzeu_AppAlertNotice, new OnResultStringListener() {
            @Override
            public void onResponse(String response) {
                AlertBean alertBean = GsonUtils.fromJson(response, AlertBean.class);

                String oldVersion = SPUtils.getInstance("app_info").getString("alert_notice_version", "0");
                final String newVersion = alertBean.getVersion();
                if (oldVersion.equals(newVersion)) {
                    return;
                }
                String title = alertBean.getTitle();
                String message = alertBean.getMessage();
                String rightText = alertBean.getRightText();
                String centerText = alertBean.getCenterText();
                String leftText = alertBean.getLeftText();
                final String canClose = alertBean.getCanClose();
                final String rightUrl = alertBean.getRightUrl();
                final String centerUrl = alertBean.getCenterUrl();
                final String leftUrl = alertBean.getLeftUrl();

                AlertDialog alertDialog = new AlertDialog.Builder(context)
                        .setTitle(title)
                        .setMessage(message)
                        .setPositiveButton(rightText, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SPUtils.getInstance("app_info").put("alert_notice_version", newVersion);
                                if (!rightUrl.isEmpty()) {
                                    MyUtils.openUrl(context, rightUrl);
                                }
                                if (!canClose.equals("否")) {
                                    MyUtils.canCloseDialog(dialog, true);
                                } else {
                                    MyUtils.canCloseDialog(dialog, false);
                                }
                            }
                        })
                        .setNegativeButton(leftText, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SPUtils.getInstance("app_info").put("alert_notice_version", newVersion);
                                MyUtils.openUrl(context, leftUrl);
                                if (!canClose.equals("否")) {
                                    MyUtils.canCloseDialog(dialog, true);
                                } else {
                                    MyUtils.canCloseDialog(dialog, false);
                                }
                            }
                        })
                        .setNeutralButton(centerText, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SPUtils.getInstance("app_info").put("alert_notice_version", newVersion);
                                MyUtils.openUrl(context, centerUrl);
                                if (!canClose.equals("否")) {
                                    MyUtils.canCloseDialog(dialog, true);
                                } else {
                                    MyUtils.canCloseDialog(dialog, false);
                                }
                            }
                        })
                        .create();
                alertDialog.show();
                alertDialog.setCanceledOnTouchOutside(false);
                if (canClose.equals("否")) {
                    alertDialog.setCancelable(false);
                }
            }

            @Override
            public void onFailure(String error) {
                LogUtils.e(error);
            }
        });

    }


    //下载封号名单
    public static void getBanUser(final Context context) {
        OkHttp.do_Get(Url.Yangtzeu_ShowBanUser, new OnResultStringListener() {
            @Override
            public void onResponse(String response) {
                BanBean banBean = GsonUtils.fromJson(response, BanBean.class);

                MyOpenHelper helper = DatabaseUtils.getHelper(context, "ban_user.db");
                if (helper.queryAll(BanBean.DataBean.class) != null) {
                    helper.clear(BanBean.DataBean.class);
                }
                if (banBean.getData() != null) {
                    for (int i = 0; i < banBean.getData().size(); i++) {
                        Log.e("封号名单", banBean.getData().get(i).getName());
                    }
                    helper.saveAll(banBean.getData());
                }

            }

            @Override
            public void onFailure(String error) {
                LogUtils.e(error);
            }
        });
    }

    //App更新检查
    public static void checkAppVersion(final Context context) {
        OkHttp.do_Get(Url.Yangtzeu_AppUp_Url, new OnResultStringListener() {
            @Override
            public void onResponse(String response) {
                UpDateBean upDateBean = GsonUtils.fromJson(response, UpDateBean.class);

                SPUtils.getInstance("app_info").put("update", "已经是最新版本！");

                //当前版本
                String oldVersion = AppUtils.getAppVersionName();
                String newAppVersion = upDateBean.getVersion();
                if (oldVersion.equals(newAppVersion)) {
                    ToastUtils.showShort("已经是最新版本");
                    return;
                }
                SPUtils.getInstance("app_info").put("update", "发现新版本！");
                String title = upDateBean.getTitle();
                String message = upDateBean.getMessage();
                String rightText = upDateBean.getRightText();
                String centerText = upDateBean.getCenterText();
                String leftText = upDateBean.getLeftText();
                String isForce = upDateBean.getForce();
                final String minVersion = upDateBean.getMinVersion();
                final String rightUrl = upDateBean.getApkurl();
                final String centerUrl = upDateBean.getBackurl();
                final String leftUrl = upDateBean.getLeftUrl();


                //当前版本
                double old_v;
                double min_v;
                try {
                    old_v = Double.parseDouble(AppUtils.getAppVersionName());
                } catch (Exception e) {
                    old_v = 2.8;
                }
                try {
                    min_v = Double.parseDouble(minVersion);
                } catch (Exception e) {
                    min_v = 0;
                }

                //判断最低升级版本
                if (old_v < min_v) {
                    isForce = "true";
                    message = message + "\n*版本" + minVersion + "以下需要更新后才能使用";
                }

                @SuppressLint("InflateParams")
                View view = LayoutInflater.from(context).inflate(R.layout.view_upadte_dialog, null);
                final AlertDialog alertDialog = new AlertDialog.Builder(context, R.style.update_dialog)
                        .setView(view).create();
                alertDialog.show();
                alertDialog.setCanceledOnTouchOutside(false);
                if (isForce.equals("true")) {
                    alertDialog.setCancelable(false);
                }

                TextView titleTv = view.findViewById(R.id.title);
                TextView versionTv = view.findViewById(R.id.version);
                TextView messageTv = view.findViewById(R.id.message);
                TextView leftBtn = view.findViewById(R.id.leftBtn);
                TextView centerBtn = view.findViewById(R.id.centerBtn);
                TextView rightBtn = view.findViewById(R.id.rightBtn);

                versionTv.setText(newAppVersion);
                titleTv.setText(title);
                messageTv.setText(message);
                leftBtn.setText(leftText);
                centerBtn.setText(centerText);
                rightBtn.setText(rightText);

                final String finalIsForce = isForce;
                leftBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MyUtils.openBrowser(context, leftUrl);
                        if (!finalIsForce.equals("true")) {
                            alertDialog.dismiss();
                        }
                    }
                });
                centerBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MyUtils.openBrowser(context, centerUrl);
                        if (!finalIsForce.equals("true")) {
                            alertDialog.dismiss();
                        }

                    }
                });
                rightBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MyUtils.openUrl(context, rightUrl);
                        if (!finalIsForce.equals("true")) {
                            alertDialog.dismiss();
                        }
                    }
                });
            }

            @Override
            public void onFailure(String error) {
                LogUtils.e(error);
            }
        });
    }

    //学生信息获取,可用于后台保持和服务器连接
    public static void getStudentInfo() {
        OkHttp.do_Get(Url.Yangtzeu_XueJI, new OnResultStringListener() {
            @Override
            public void onResponse(String response) {
                Document doc = Jsoup.parse(response);
                if (doc.text().contains("学籍信息")) {
                    LogUtils.e("学籍信息获取成功");
                    try {
                        Elements studentInfoTb = doc.select("table#studentInfoTb tbody tr");
                        String studentInfoTb1 = studentInfoTb.get(1).text();
                        String[] student_list1 = studentInfoTb1.split(" ");
                        String studentInfoTb11 = studentInfoTb.get(11).text();
                        String[] student_list10 = studentInfoTb11.split(" ");
                        //姓名
                        String userName = student_list1[student_list1.length - 1];
                        //保存姓名
                        SPUtils.getInstance("user_info").put("name", userName);
                        //班级
                        String userClass = student_list10[student_list10.length - 1];
                        //保存班级
                        SPUtils.getInstance("user_info").put("class", userClass);

                        String number = SPUtils.getInstance("user_info").getString("number");
                        String password = SPUtils.getInstance("user_info").getString("password");

                        UserBean userBean = new UserBean();
                        userBean.setName(userName);
                        userBean.setId(Long.parseLong(number));
                        userBean.setNumber(number);
                        userBean.setPassowrd(password);

                        DatabaseUtils.getHelper(ActivityUtils.getTopActivity(), "user.db").save(userBean);
                    } catch (Exception e) {
                        LogUtils.e("学籍信息解析出错", e);
                    }
                } else if (doc.text().contains("重复登录") || doc.text().contains("用户名")) {
                    //设置Cookie不可用
                    SPUtils.getInstance("user_info").put("online", false);
                    ActivityUtils.finishAllActivities();
                    MyUtils.startActivity(LoginActivity.class);
                } else {
                    LogUtils.e("学籍信息获取失败");
                }
            }

            @Override
            public void onFailure(String error) {
                LogUtils.e(error);
            }
        });
    }


    //选择年份
    public static void showChooseTerm(Activity activity, final DialogInterface.OnClickListener listener) {
        final String[] term_trip = activity.getResources().getStringArray(R.array.term_trip);
        final String[] term_id = activity.getResources().getStringArray(R.array.term_id);
        @SuppressLint("InflateParams")
        View view = activity.getLayoutInflater().inflate(R.layout.view_choose_term, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.show();
        LinearLayout layout = view.findViewById(R.id.container);
        for (int i = 0; i < term_trip.length; i++) {
            @SuppressLint("InflateParams")
            View item = activity.getLayoutInflater().inflate(R.layout.view_choose_term_item, null);
            TextView title = item.findViewById(R.id.title);
            TextView bt = item.findViewById(R.id.bt);
            title.setText(term_trip[i]);
            final int finalI = i;
            bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    ToastUtils.showLong("你选择了：" + term_trip[finalI]);
                    int which = Integer.parseInt(term_id[finalI]);
                    listener.onClick(null, which);
                }
            });
            layout.addView(item);
        }
    }

    //检查当前是否有课程
    public static void checkHaveClassNow(Context context, OnClassListener listener) {
        List<Course> now_course = new ArrayList<>();
        List<Course> courses = DatabaseUtils.getHelper(context, "table.db").queryAll(Course.class);
        if (ObjectUtils.isNotEmpty(courses)) {
            for (int i = 0; i < courses.size(); i++) {
                int week_day = Integer.parseInt(courses.get(i).getWeek()) + 1;
                int now_week_day = MyUtils.getIntStringWeek();
                if (week_day == now_week_day) {
                    now_course.add(courses.get(i));
                }
            }
            listener.onClass(now_course);
        } else {
            listener.onClass(now_course);
        }
    }

    //在线人数统计
    public static void keepOnline(final OnResultListener<OnLineBean> listener) {
        String name = SPUtils.getInstance("user_info").getString("name");
        String number = SPUtils.getInstance("user_info").getString("number");
        String url = Url.Yangtzeu_App_Online + "?name=" + name + "&number=" + number;
        OkHttp.do_Get(url, new OnResultStringListener() {
            @Override
            public void onResponse(String response) {
                OnLineBean bean = GsonUtils.fromJson(response, OnLineBean.class);
                if (listener != null) {
                    listener.onResult(bean);
                }
            }

            @Override
            public void onFailure(String error) {
                LogUtils.e(error);
            }
        });
    }


    public static double getSectionTimeSpan() {
        // 获取当前日期
        Calendar cal = Calendar.getInstance();
        // 获取当前小时
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        // 获取当前小时内分钟
        int min = cal.get(Calendar.MINUTE);
        //凌晨开始计时到当前的分钟数
        int minuteOfDay = hour * 60 + min;

        if (minuteOfDay < StudyTimeTable.Time_Section1_Start) {
            return 0;
        } else if (minuteOfDay < StudyTimeTable.Time_Section2_End) {
            return 1;
        } else if (minuteOfDay < StudyTimeTable.Time_Section3_Start) {
            return 1.5;
        } else if (minuteOfDay < StudyTimeTable.Time_Section4_End) {
            return 2;
        } else if (minuteOfDay < StudyTimeTable.Time_Section5_Start) {
            return 2.5;
        } else if (minuteOfDay < StudyTimeTable.Time_Section6_End) {
            return 3;
        } else if (minuteOfDay < StudyTimeTable.Time_Section7_Start) {
            return 3.5;
        } else if (minuteOfDay < StudyTimeTable.Time_Section8_End) {
            return 4;
        } else if (minuteOfDay < StudyTimeTable.Time_Section9_Start) {
            return 4.5;
        } else if (minuteOfDay < StudyTimeTable.Time_Section10_End) {
            return 5;
        } else {
            return 6;
        }
    }

    //弹出完善身份信息的对话框
    public static void inputInfoAlert(Context context) {
        @SuppressLint("InflateParams")
        View view = LayoutInflater.from(context).inflate(R.layout.view_user_info, null);
        final TextInputEditText phoneView = view.findViewById(R.id.phone);
        final TextInputEditText qqView = view.findViewById(R.id.qq);
        final TextInputEditText we_chatView = view.findViewById(R.id.we_chat);
        AlertDialog alert = MyUtils.getAlert(context, null, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String phone = Objects.requireNonNull(phoneView.getText()).toString().trim();
                String wechat = Objects.requireNonNull(we_chatView.getText()).toString().trim();
                String qq = Objects.requireNonNull(qqView.getText()).toString().trim();
                if (phone.isEmpty() || wechat.isEmpty() || qq.isEmpty()) {
                    ToastUtils.showShort(R.string.please_input);
                } else {
                    SPUtils.getInstance("user_info").put("wechat", wechat);
                    SPUtils.getInstance("user_info").put("qq", qq);
                    SPUtils.getInstance("user_info").put("phone", phone);
                }
            }
        });
        alert.setTitle(null);
        alert.setView(view);
        alert.show();
    }

    //获取锁屏白名单
    public static void getLockPhoneWhiteUser() {
        OkHttp.do_Get(Url.Yangtzeu_App_Lock_White, new OnResultStringListener() {
            @Override
            public void onResponse(String response) {
                MessageBean messageBean = GsonUtils.fromJson(response, MessageBean.class);
                String info = messageBean.getInfo();
                if (info.contains(IMManager.getUser().getId())) {
                    //进程不同，用文件存储数据，以便时实更新
                    CacheDiskUtils.getInstance(MyUtils.geCacheDir()).put("lock_time", "0");
                }
            }

            @Override
            public void onFailure(String error) {
                LogUtils.e(error);
            }
        });
    }

    //获取统计点击数
    public static void getOnClickTimes(TextView keyTextView, final TextView jiTextView,boolean isAdd) {
        String name = keyTextView.getText().toString().trim();
        String key = EncryptUtils.encryptMD5ToString(name);

        OkHttp.do_Get(Url.getTongJi(key, name,isAdd), new OnResultStringListener() {
            @Override
            public void onResponse(String response) {
                MessageBean bean= GsonUtils.fromJson(response,MessageBean.class);
                jiTextView.setText(bean.getInfo());
            }

            @Override
            public void onFailure(String error) {
                jiTextView.setText("统计失败");
            }
        });
    }

    public static String getStudyTimeSpan() {
        // 获取当前日期
        Calendar cal = Calendar.getInstance();
        long fain = TimeUtils.string2Millis("2019-01-21 00:00:00");
        long next_term = TimeUtils.string2Millis("2019-01-21 00:00:00");
        if (fain < TimeUtils.getNowMills() && TimeUtils.getNowMills() < next_term) {
            return "现在是寒假时间，赶紧好好地的玩吧！";
        }

        // 获取当前小时
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        // 获取当前小时内分钟
        int min = cal.get(Calendar.MINUTE);
        //凌晨开始计时到当前的分钟数
        int minuteOfDay = hour * 60 + min;

        if (minuteOfDay < StudyTimeTable.Time_Get_Up) {
            return "现在是睡觉时间，赶紧休息吧！";
        } else if (minuteOfDay < StudyTimeTable.Time_BreakFast) {
            return "快点起床，要上课啦！";
        } else if (minuteOfDay < StudyTimeTable.Time_Ready_Am) {
            int rest = (StudyTimeTable.Time_Ready_Am - minuteOfDay);
            return "还有" + rest + "分钟才到预备时间，先吃一个早餐吧！";
        } else if (minuteOfDay < StudyTimeTable.Time_Section1_Start) {
            int rest = (StudyTimeTable.Time_Section1_Start - minuteOfDay);
            return "现在是预备时间哦，还有" + rest + "分钟就上课咯！";
        } else if (minuteOfDay < StudyTimeTable.Time_Section1_End) {
            int rest = (StudyTimeTable.Time_Section1_End - minuteOfDay);
            return "第一小节课要认真哦，离下课还有" + rest + "分钟！";
        } else if (minuteOfDay < StudyTimeTable.Time_Section2_Start) {
            int rest = (StudyTimeTable.Time_Section2_Start - minuteOfDay);
            return "第一小节下课啦，还有" + rest + "分钟就上课啦！";
        } else if (minuteOfDay < StudyTimeTable.Time_Section2_End) {
            int rest = (StudyTimeTable.Time_Section2_End - minuteOfDay);
            return "第二小节课也要打起精神来，离下课还有" + rest + "分钟！";
        } else if (minuteOfDay < StudyTimeTable.Time_Section3_Start) {
            int rest = (StudyTimeTable.Time_Section3_Start - minuteOfDay);
            return "第二小节下课啦，你还有" + rest + "分钟的休息时间！";
        } else if (minuteOfDay < StudyTimeTable.Time_Section3_End) {
            int rest = (StudyTimeTable.Time_Section3_End - minuteOfDay);
            return "第三小节上课时间，还有" + rest + "分钟下课！";
        } else if (minuteOfDay < StudyTimeTable.Time_Section4_Start) {
            int rest = (StudyTimeTable.Time_Section4_Start - minuteOfDay);
            return "第三小节下课，还有" + rest + "分钟上课！";
        } else if (minuteOfDay < StudyTimeTable.Time_Section4_End) {
            int rest = (StudyTimeTable.Time_Section4_End - minuteOfDay);
            return "第四小节还有" + rest + "分钟就完啦！";
        } else if (minuteOfDay < StudyTimeTable.Time_Ready_Go) {
            return "现在是午餐午休时间哦！";
        } else if (minuteOfDay < StudyTimeTable.Time_Ready_Pm) {
            return "快点准备去上课啦！";
        } else if (minuteOfDay < StudyTimeTable.Time_Section5_Start) {
            int rest = (StudyTimeTable.Time_Section5_Start - minuteOfDay);
            return "预备时间，先看看书，还有" + rest + "分钟上课！";
        } else if (minuteOfDay < StudyTimeTable.Time_Section5_End) {
            int rest = (StudyTimeTable.Time_Section5_End - minuteOfDay);
            return "第五小节是不是有点困呀，还有" + rest + "分钟下课！";
        } else if (minuteOfDay < StudyTimeTable.Time_Section6_Start) {
            int rest = (StudyTimeTable.Time_Section6_Start - minuteOfDay);
            return "第五小节下课啦，小憩一会儿，" + rest + "分钟后上课！";
        } else if (minuteOfDay < StudyTimeTable.Time_Section6_End) {
            int rest = (StudyTimeTable.Time_Section6_End - minuteOfDay);
            return "现在是第六小节时间，还有" + rest + "分钟下课！";
        } else if (minuteOfDay < StudyTimeTable.Time_Section7_Start) {
            int rest = (StudyTimeTable.Time_Section7_Start - minuteOfDay);
            return "第六小节下课啦，还有" + rest + "分钟上课哦！";
        } else if (minuteOfDay < StudyTimeTable.Time_Section7_End) {
            int rest = (StudyTimeTable.Time_Section7_End - minuteOfDay);
            return "正在上第七小节呢，还有" + rest + "分钟才能休息！";
        } else if (minuteOfDay < StudyTimeTable.Time_Section8_Start) {
            int rest = (StudyTimeTable.Time_Section8_Start - minuteOfDay);
            return "第七小节下课咯，离上课还有" + rest + "分钟！";
        } else if (minuteOfDay < StudyTimeTable.Time_Section8_End) {
            int rest = (StudyTimeTable.Time_Section8_End - minuteOfDay);
            return "第八小节还有" + rest + "分钟就完啦！";
        } else if (minuteOfDay < StudyTimeTable.Time_Ready_Nm) {
            return "现在是晚餐时间哦！";
        } else if (minuteOfDay < StudyTimeTable.Time_Section9_Start) {
            int rest = (StudyTimeTable.Time_Section9_Start - minuteOfDay);
            return "现在是晚自习预备时间，还有" + rest + "分钟上课！";
        } else if (minuteOfDay < StudyTimeTable.Time_Section9_End) {
            int rest = (StudyTimeTable.Time_Section9_End - minuteOfDay);
            return "现在第九小节课进行时，还有" + rest + "分钟下课！";
        } else if (minuteOfDay < StudyTimeTable.Time_Section10_Start) {
            return "第九小节下课啦！";
        } else if (minuteOfDay < StudyTimeTable.Time_Section10_End) {
            int rest = (StudyTimeTable.Time_Section10_End - minuteOfDay);
            return "第十小节上课时间，" + rest + "分钟后下课！";
        } else if (minuteOfDay < StudyTimeTable.Time_Section11_Start) {
            return "第十小节下课啦，后面应该没课了吧！";
        } else if (minuteOfDay < StudyTimeTable.Time_Section11_End) {
            return "现在是第十一小节，在学习的都是大佬！";
        } else if (minuteOfDay < StudyTimeTable.Time_Section12_Start) {
            return "第十一小节下课了，休息一会吧！";
        } else if (minuteOfDay < StudyTimeTable.Time_Section12_End) {
            return "学习一天了，休息一会吧！";
        } else if (minuteOfDay < StudyTimeTable.Time_Sleep) {
            return "现在时刻，教室、图书馆关门！";
        } else {
            return "时间不早了，早点休息吧！";
        }


    }

    public static class StudyTimeTable {
        public static final int Time_Get_Up = 6 * 60 + 30;

        public static final int Time_BreakFast = 7 * 60;

        public static final int Time_Ready_Am = 7 * 60 + 50;

        public static final int Time_Section1_Start = 8 * 60;
        public static final int Time_Section1_End = 8 * 60 + 45;

        public static final int Time_Section2_Start = 8 * 60 + 50;
        public static final int Time_Section2_End = 9 * 60 + 35;

        public static final int Time_Section3_Start = 10 * 60 + 5;
        public static final int Time_Section3_End = 10 * 60 + 50;

        public static final int Time_Section4_Start = 10 * 60 + 55;
        public static final int Time_Section4_End = 11 * 60 + 40;

        public static final int Time_Ready_Go = 13 * 60 + 30;

        public static final int Time_Ready_Pm = 13 * 60 + 50;

        public static final int Time_Section5_Start = 14 * 60;
        public static final int Time_Section5_End = 14 * 60 + 45;

        public static final int Time_Section6_Start = 14 * 60 + 50;
        public static final int Time_Section6_End = 15 * 60 + 35;

        public static final int Time_Section7_Start = 16 * 60 + 5;
        public static final int Time_Section7_End = 16 * 60 + 50;

        public static final int Time_Section8_Start = 16 * 60 + 55;
        public static final int Time_Section8_End = 17 * 60 + 40;

        public static final int Time_Ready_Nm = 18 * 60 + 50;

        public static final int Time_Section9_Start = 19 * 60;
        public static final int Time_Section9_End = 19 * 60 + 45;

        public static final int Time_Section10_Start = 19 * 60 + 50;
        public static final int Time_Section10_End = 20 * 60 + 35;

        public static final int Time_Section11_Start = 20 * 60 + 45;
        public static final int Time_Section11_End = 21 * 60 + 30;

        public static final int Time_Section12_Start = 21 * 60 + 35;
        public static final int Time_Section12_End = 22 * 60 + 20;

        public static final int Time_Sleep = 22 * 60 + 30;
    }
}
