package com.yangtzeu.model;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.SnackbarUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.yangtzeu.R;
import com.yangtzeu.entity.GradeBean;
import com.yangtzeu.http.OkHttp;
import com.yangtzeu.http.OnResultStringListener;
import com.yangtzeu.model.imodel.IGradePart1Model;
import com.yangtzeu.ui.activity.LoginActivity;
import com.yangtzeu.ui.view.GradePartView1;
import com.yangtzeu.url.Url;
import com.yangtzeu.utils.MyUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class GradePart1Model implements IGradePart1Model {
    private String term_id;

    @Override
    public void loadGradeData(Activity activity, GradePartView1 view) {
        term_id = SPUtils.getInstance("user_info").getString("term_id", Url.Default_Term);
        final String[] term_trip = activity.getResources().getStringArray(R.array.term_trip);
        final String[] term_id = activity.getResources().getStringArray(R.array.term_id);
        for (int i = 0; i < term_id.length; i++) {
            if (this.term_id.equals(term_id[i])) {
                view.getToolbar().setTitle(term_trip[i]);
            }
        }

        view.getAdapter().clear();
        view.getGradeBeans().clear();

        requestGradeData(activity, view);
    }

    @Override
    public void requestGradeData(final Activity activity, final GradePartView1 view) {
        final String url = view.getUrl() + term_id;
        OkHttp.do_Get(url, new OnResultStringListener() {
            @Override
            public void onResponse(String response) {
                view.getRefresh().finishRefresh();
                if (response.contains("请不要过快点击") || response.contains("重复登录")) {
                    requestGradeData(activity, view);
                } else if (response.contains("课程名称")) {
                    if (response.contains("总评成绩")) {
                        parseGrade(activity, view, response);
                    } else {
                        final int now_term = Integer.parseInt(term_id);
                        int before = Integer.parseInt(term_id) - 1;

                        if (before == 47) before = 46;
                        if (before == 68) before = 49;

                        final int finalBefore = before;
                        SnackbarUtils.with(view.getRecyclerView()).setMessage("当前学期（ID：" + now_term + "）没有成绩数据")
                                .setDuration(SnackbarUtils.LENGTH_LONG)
                                .setAction("上学期成绩", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        term_id = String.valueOf(finalBefore);
                                        requestGradeData(activity, view);
                                    }
                                }).show();
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
    public void parseGrade(final Activity activity, final GradePartView1 view, String data) {
        Document document = Jsoup.parse(data);
        Elements elements = document.select("div.grid table.gridtable tbody tr");
        int Column = document.select("div.grid table.gridtable thead tr th").size();

        if (Column == 10) {
            //含补考
            for (Element element : elements) {
                Elements tds = element.select("tr td");
                GradeBean gradeBean = new GradeBean();

                String[] YearTerm = tds.get(0).text().split(" ");
                String courseYear = YearTerm[0];
                String courseTerm = YearTerm[1];

                String courseCode = tds.get(1).text();
                String courseIndex = tds.get(2).text();
                String courseName = tds.get(3).text();
                String courseKind = tds.get(4).text();
                String courseScore = tds.get(5).text();
                String courseBuKao = tds.get(6).text();
                String courseZongPing = tds.get(7).text();
                String courseZuiZhong = tds.get(8).text();
                String coursePoint = tds.get(9).text();

                gradeBean.setCourseYear(courseYear);
                gradeBean.setCourseTerm(courseTerm);
                gradeBean.setCourseCode(courseCode);
                gradeBean.setCourseIndex(courseIndex);
                gradeBean.setCourseName(courseName);
                gradeBean.setCourseKind(courseKind);
                gradeBean.setCourseScore(courseScore);
                gradeBean.setCourseBuKao(courseBuKao);
                gradeBean.setCourseZongPing(courseZongPing);
                gradeBean.setCourseZuiZhong(courseZuiZhong);
                gradeBean.setCoursePoint(coursePoint);

                view.getGradeBeans().add(gradeBean);
            }
        }

        if (Column == 9) {
            //不含补考
            for (Element element : elements) {
                Elements tds = element.select("tr td");
                GradeBean gradeBean = new GradeBean();

                String[] YearTerm = tds.get(0).text().split(" ");
                String courseYear = YearTerm[0];
                String courseTerm = YearTerm[1];

                String courseCode = tds.get(1).text();
                String courseIndex = tds.get(2).text();
                String courseName = tds.get(3).text();
                String courseKind = tds.get(4).text();
                String courseScore = tds.get(5).text();
                String courseZongPing = tds.get(6).text();
                String courseZuiZhong = tds.get(7).text();
                String coursePoint = tds.get(8).text();

                gradeBean.setCourseYear(courseYear);
                gradeBean.setCourseTerm(courseTerm);
                gradeBean.setCourseCode(courseCode);
                gradeBean.setCourseIndex(courseIndex);
                gradeBean.setCourseName(courseName);
                gradeBean.setCourseKind(courseKind);
                gradeBean.setCourseScore(courseScore);
                gradeBean.setCourseBuKao(null);
                gradeBean.setCourseZongPing(courseZongPing);
                gradeBean.setCourseZuiZhong(courseZuiZhong);
                gradeBean.setCoursePoint(coursePoint);

                view.getGradeBeans().add(gradeBean);

            }
        }

        view.getAdapter().setData(view.getGradeBeans());
        view.getAdapter().notifyItemRangeChanged(0, view.getAdapter().getItemCount());
    }
}
