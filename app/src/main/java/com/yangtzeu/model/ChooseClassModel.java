package com.yangtzeu.model;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.yangtzeu.R;
import com.yangtzeu.http.OkHttp;
import com.yangtzeu.http.OnResultStringListener;
import com.yangtzeu.model.imodel.IChooseClassModel;
import com.yangtzeu.ui.activity.LoginActivity;
import com.yangtzeu.ui.view.ChooseClassView;
import com.yangtzeu.utils.MyUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class ChooseClassModel implements IChooseClassModel {

    @Override
    public void getChooseClassInfo(final Activity activity, final ChooseClassView view) {
        OkHttp.do_Get(view.getUrl(), new OnResultStringListener() {
            @Override
            public void onFailure(String error) {
                if (view.getTrip() != null)
                    view.getTrip().setText("请点击屏幕刷新");
                view.getContainer().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getChooseClassInfo(activity, view);
                        if (view.getTrip() != null)
                            view.getTrip().setText("玩命加载中...\n请稍等!");
                    }
                });
            }

            @Override
            public void onResponse(String result) {
                if (result.contains("请不要过快点击")) {
                    getChooseClassInfo(activity, view);
                } else if (result.contains("请输入用户名") || result.contains("重复登录")) {
                    SPUtils.getInstance("user_info").put("online", false);
                    AlertDialog dialog = new AlertDialog.Builder(activity)
                            .setTitle("温馨提示")
                            .setMessage("账户过期，请重新登录")
                            .setPositiveButton("去登录", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ActivityUtils.finishAllActivities();
                                    MyUtils.startActivity(LoginActivity.class);
                                }
                            })
                            .setNegativeButton("取消", null)
                            .create();
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();
                } else if (result.contains("未到选课时间")) {
                    MyUtils.getAlert(activity, activity.getString(R.string.choose_class_time_error), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            activity.onBackPressed();
                        }
                    }).show();
                } else {
                    Document document = Jsoup.parse(result);
                    Elements ajax_container_div = document.select("div.ajax_container div.ajax_container");
                    ajax_container_div.select("script").remove();
                    view.getContainer().removeAllViews();

                    for (int i = 0; i < ajax_container_div.size(); i++) {
                        int pad = ConvertUtils.dp2px(15);

                        View m_view = new View(activity);
                        m_view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, pad));
                        m_view.setBackgroundColor(activity.getResources().getColor(R.color.black_20));

                        TextView textView = new TextView(activity);
                        textView.setPadding(pad, pad, pad, pad);
                        textView.setGravity(Gravity.START);
                        textView.setText(Html.fromHtml(ajax_container_div.get(i).toString()));
                        view.getContainer().addView(textView);
                        view.getContainer().addView(m_view);

                        final String url_end = ajax_container_div.get(i).select("a").attr("href").trim();
                        textView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (ObjectUtils.isEmpty(url_end)) {
                                    ToastUtils.showShort("选课时间未到");
                                } else {
                                    MyUtils.openUrl(activity, "http://221.233.24.23" + url_end, true);
                                }
                            }
                        });
                    }

                }

            }
        });
    }
}
