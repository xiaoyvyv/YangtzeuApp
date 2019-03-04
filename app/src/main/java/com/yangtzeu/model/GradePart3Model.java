package com.yangtzeu.model;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.SnackbarUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.yangtzeu.R;
import com.yangtzeu.entity.PointBean;
import com.yangtzeu.http.OkHttp;
import com.yangtzeu.http.OnResultStringListener;
import com.yangtzeu.model.imodel.IGradePart3Model;
import com.yangtzeu.ui.activity.LoginActivity;
import com.yangtzeu.ui.view.GradePartView2;
import com.yangtzeu.ui.view.GradePartView3;
import com.yangtzeu.utils.MyUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.List;


public class GradePart3Model implements IGradePart3Model {


    @Override
    public void loadPointData(final Activity activity, final GradePartView3 view) {
        OkHttp.do_Get(view.getUrl(), new OnResultStringListener() {
            @Override
            public void onResponse(String response) {
                view.getRefresh().finishRefresh();
                if (response.contains("请不要过快点击") || response.contains("重复登录")) {
                    loadPointData(activity, view);
                } else if (response.contains("课程名称")) {
                    if (response.contains("总评成绩")) {
                        parseAllGrade(activity, view, response);
                    } else {
                        SnackbarUtils.with(view.getRecyclerView()).setMessage("未查询到绩点数据")
                                .setDuration(SnackbarUtils.LENGTH_SHORT).show();
                    }
                } else {
                    //设置Cookie不可用
                    SPUtils.getInstance("user_info").put("online", false);
                    AlertDialog dialog = MyUtils.getAlert(activity, activity.getString(R.string.user_outline), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            MyUtils.startActivity(LoginActivity.class);
                        }
                    });
                    dialog.show();
                }
            }

            @Override
            public void onFailure(String error) {
                view.getRefresh().finishRefresh();
                ToastUtils.showShort(R.string.refresh_again);
            }
        });
    }

    @Override
    public void parseAllGrade(final Activity activity, final GradePartView3 view, String result) {
        view.getHeaderContainer().setVisibility(View.VISIBLE);

        Document document = Jsoup.parse(result);
        Elements elements = document.select("table.gridtable tbody");
        Elements pointElements = elements.get(0).select("tbody tr");

        //绩点容器
        List<PointBean> gradeBeans = view.getGradeBeans();
        for (int i = 0; i < pointElements.size(); i++) {
            try {
                Element element = pointElements.get(i);
                Elements tds = element.select("tr td");
                PointBean pointBean = new PointBean();

                if (i == pointElements.size() - 2) {
                    Elements th = element.select("tr th");
                    view.getAllNumberView().setText(th.get(1).text());
                    view.getAllScoreView().setText(th.get(2).text());
                    view.getAllPointView().setText(th.get(3).text());
                    continue;
                }
                if (i == pointElements.size() - 1) {
                    continue;
                }

                String courseYear = tds.get(0).text();
                String courseTerm = tds.get(1).text();
                String courseNumber = tds.get(2).text();
                String courseScore = tds.get(3).text();
                String coursePoint = tds.get(4).text();

                pointBean.setCourseYear(courseYear);
                pointBean.setCourseTerm(courseTerm);
                pointBean.setCourseNumber(courseNumber);
                pointBean.setCourseScore(courseScore);
                pointBean.setCoursePoint(coursePoint);

                LogUtils.e(courseYear, courseTerm, courseNumber, courseScore, coursePoint);
                gradeBeans.add(pointBean);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        view.getPointAdapter().setData(gradeBeans);
        view.getPointAdapter().notifyItemRangeChanged(0, view.getPointAdapter().getItemCount());
    }

}
