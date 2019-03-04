package com.yangtzeu.ui.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yangtzeu.R;
import com.yangtzeu.http.OkHttp;
import com.yangtzeu.http.OnResultStringListener;
import com.yangtzeu.ui.activity.base.BaseFragment;
import com.yangtzeu.url.Url;
import com.yangtzeu.utils.MyUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.Objects;

import androidx.annotation.NonNull;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by Administrator on 2018/2/4.
 */

public class PhysicalFragment extends BaseFragment {
    // 标志位，标志已经初始化完成。
    private boolean isPrepared = false;
    private boolean isLoadFinish = false;

    private View InflateView;
    private SmartRefreshLayout refresh;
    private LinearLayout container;
    private String from_url;

    public static PhysicalFragment newInstance(String from_url) {
        PhysicalFragment fragment = new PhysicalFragment();
        Bundle bundle = new Bundle();
        bundle.putString("from_url", from_url);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if (bundle != null) {
            from_url = bundle.getString("from_url");
        }
        InflateView = inflater.inflate(R.layout.fragment_physical, container, false);
        findViews();
        isPrepared = true;
        lazyLoad();
        return InflateView;
    }


    @Override
    public void findViews() {
        container = InflateView.findViewById(R.id.slow_container);
        refresh = InflateView.findViewById(R.id.refresh);


    }

    @Override
    public void setEvents() {

        refresh.setRefreshHeader(new MaterialHeader(Objects.requireNonNull(getActivity())));
        refresh.setEnableLoadMore(false);
        refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                getData();
            }
        });
        refresh.autoRefresh();
    }

    private void getData() {
        OkHttp.do_Get(from_url, new OnResultStringListener() {
            @Override
            public void onResponse(String response) {
                Document document = Jsoup.parse(response);
                Elements trs = document.select("table.table3>tbody>tr");
                if (response.contains("未登录")) {
                    AlertDialog dialog = MyUtils.getAlert(getActivity(), "登录过期，请重新登录", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
                    dialog.show();
                } else {
                    fitView(trs);
                }
                refresh.finishLoadMore();
                refresh.finishRefresh();
            }

            @Override
            public void onFailure(String error) {
                refresh.finishLoadMore();
                refresh.finishRefresh();
                ToastUtils.showShort(R.string.try_again);
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void fitView(Elements trs) {
        container.removeAllViews();
        int isize = 0;
        if (from_url.equals(Url.Yangtzeu_Physical_Grade)) {
            if (trs.size() == 4) {
                toAdd();
                return;
            }
            isize = trs.size() - 3;
        } else if (from_url.equals(Url.Yangtzeu_Physical_Delete)) {
            if (trs.size() == 1) {
                toAdd();
                return;
            }
            isize = trs.size() - 1;
        }

        for (int i = 1; i < isize; i++) {
            @SuppressLint("InflateParams")
            View view = Objects.requireNonNull(getActivity()).getLayoutInflater().inflate(R.layout.fragment_physical_item, null);
            container.addView(view);

            TextView st_name = view.findViewById(R.id.st_name);
            TextView st_b_grade = view.findViewById(R.id.st_b_grade);
            TextView st_c_grade = view.findViewById(R.id.st_c_grade);
            TextView st_local = view.findViewById(R.id.st_local);
            TextView st_week = view.findViewById(R.id.st_week);
            TextView st_day = view.findViewById(R.id.st_day);
            TextView st_which = view.findViewById(R.id.st_which);
            LinearLayout onClick = view.findViewById(R.id.onClick);

            Elements tds = trs.get(i).select("tr td");

            String name = "";
            String b_grade = "";
            String c_grade = "";
            String week = "";
            String day = "";
            String which = "";
            String local = "";
            String delete_id = "";
            //我的预约界面
            if (from_url.equals(Url.Yangtzeu_Physical_Grade)) {
                try {
                    name = tds.get(1).text();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    week = tds.get(2).text();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    day = tds.get(3).text();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    which = tds.get(4).text();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    local = tds.get(5).text();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    b_grade = tds.get(7).text();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    c_grade = tds.get(6).text();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                st_name.setText("实验名称：" + name);
                st_b_grade.setText("报告成绩：" + b_grade);
                st_c_grade.setText("操作成绩：" + c_grade);
                st_week.setText("实验周次：" + week);
                st_day.setText("实验星期：" + day);
                st_which.setText("实验节次：" + which);
                st_local.setText("实验地点：" + local);


                final String details = "实验名称：" + name
                        + "\n实验周次：" + week
                        + "\n实验星期：" + day
                        + "\n实验节次：" + which
                        + "\n实验地点：" + local
                        + "\n报告成绩：" + b_grade
                        + "\n操作成绩：" + c_grade;

                onClick.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                                .setTitle("成绩详情")
                                .setMessage(details)
                                .setPositiveButton("知道了", null)
                                .setNegativeButton("分享实验", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        MyUtils.shareText(getActivity(), "给你看看我的实验噢!\n\n" + details + "\n\n数据来自：" + Url.AppDownUrl);
                                    }
                                })
                                .create();
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.show();
                    }
                });
            }
            //删除我的预约界面
            else if (from_url.equals(Url.Yangtzeu_Physical_Delete)) {
                try {
                    name = tds.get(0).text();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    week = tds.get(1).text();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    day = tds.get(2).text();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    which = tds.get(3).text();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    c_grade = tds.get(4).text();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    b_grade = tds.get(5).text();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    local = tds.get(6).text();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    delete_id = tds.get(7).select("td>input").attr("value");
                    LogUtils.e("删除Id:" + delete_id);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                st_name.setText("实验名称：" + name);
                st_b_grade.setText("报告成绩：" + b_grade);
                st_c_grade.setText("操作成绩：" + c_grade);
                st_week.setText("实验周次：" + week);
                st_day.setText("实验星期：" + day);
                st_which.setText("实验节次：" + which);
                st_local.setText("实验旷课：" + local);

                final String finalDelete_id = delete_id;
                final String finalName = name;
                onClick.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                                .setTitle(R.string.trip)
                                .setMessage("您确定删除《" + finalName + "》实验预约？")
                                .setNegativeButton(R.string.clear, null)
                                .setPositiveButton(R.string.done, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        RequestBody body = new FormBody.Builder()
                                                .add("del_id[]", finalDelete_id)
                                                .add("Submit", "删除预约")
                                                .build();
                                        Request request = new Request.Builder()
                                                .url(from_url)
                                                .post(body)
                                                .build();
                                        OkHttp.do_Post(request, new OnResultStringListener() {
                                            @Override
                                            public void onResponse(String response) {
                                                ToastUtils.showShort("删除成功");
                                                getData();
                                            }

                                            @Override
                                            public void onFailure(String error) {
                                                ToastUtils.showShort("删除失败");
                                            }
                                        });
                                    }
                                }).create();
                        dialog.show();

                    }
                });
            }
        }

    }

    private void toAdd() {
        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.trip)
                .setMessage("您当前未预约任何物理实验，请点击添加")
                .setPositiveButton("知道了", null)
                .create();
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
    }

    @Override
    protected void lazyLoad() {
        if (!isPrepared || !isVisible) {
            return;
        }
        if (!isLoadFinish) {
            setEvents();
            isLoadFinish = true;
        }
    }
}