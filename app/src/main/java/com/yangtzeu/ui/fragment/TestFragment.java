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
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
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
import androidx.cardview.widget.CardView;
import okhttp3.Request;

/**
 * Created by Administrator on 2018/2/4.
 */

public class TestFragment extends BaseFragment {
    // 标志位，标志已经初始化完成。
    private boolean isPrepared = false;
    private boolean isLoadFinish = false;

    private View rootView;
    private SmartRefreshLayout refresh;
    private LinearLayout container;
    private String examBatch_id;
    private String title;

    public static TestFragment newInstance(String title, String examBatch_id) {
        TestFragment fragment = new TestFragment();
        Bundle bundle = new Bundle();
        bundle.putString("examBatch_id", examBatch_id);
        bundle.putString("title", title);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if (bundle != null) {
            title = bundle.getString("title");
            examBatch_id = bundle.getString("examBatch_id");
            LogUtils.e(title, examBatch_id);
        }
        rootView = inflater.inflate(R.layout.fragment_test, container, false);
        findViews();
        isPrepared = true;
        lazyLoad();
        return rootView;
    }


    @Override
    public void findViews() {

        refresh = rootView.findViewById(R.id.refresh);
        container = rootView.findViewById(R.id.container);
        refresh = rootView.findViewById(R.id.refresh);

    }

    @Override
    public void setEvents() {

        refresh.setEnableLoadMore(false);
        refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                GetTable();
            }
        });
        refresh.autoRefresh();
    }

    private void GetTable() {
        String cookie = SPUtils.getInstance("user_info").getString("cookie", "no_cookie");

        Request request = new Request.Builder()
                .url(Url.Yangtzeu_My_Details_Test + examBatch_id)
                .addHeader("Cookie", cookie)
                .build();

        OkHttp.do_Post(request, new OnResultStringListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(String result) {
                refresh.finishRefresh();
                try {
                    Document document = Jsoup.parse(result);
                    Elements trs = document.select("table.gridtable>tbody>tr");
                    if (ObjectUtils.isEmpty(trs.text().trim())) {
                        final AlertDialog dialog = new AlertDialog.Builder(getActivity())
                                .setTitle("提示")
                                .setMessage("当前选择批次：" + title + "\n暂时没有考试安排\n\n恭喜你不需要参加考试")
                                .setPositiveButton("知道了", null)
                                .create();
                        dialog.show();
                        dialog.setCanceledOnTouchOutside(false);
                        return;
                    }
                    container.removeAllViews();

                    for (int i = 0; i < trs.size(); i++) {
                        @SuppressLint("InflateParams")
                        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_test_item, null);
                        container.addView(view);
                        Elements tds = trs.get(i).select("tr td");

                        String xvhao;
                        try {
                            xvhao = tds.get(0).text();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        String name = "";
                        try {
                            name = tds.get(1).text();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        String kind = "";
                        try {
                            kind = tds.get(2).text();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        String data = "";
                        try {
                            data = tds.get(3).text();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        String plan = "";
                        try {
                            plan = tds.get(4).text();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        String local = "";
                        String local_url = "";
                        try {
                            local = tds.get(5).text();
                            local_url = Url.Yangtzeu_Base_Url + tds.get(5).select("td a").attr("href");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        String which = "";
                        try {
                            which = tds.get(6).text();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        String state = "";
                        try {
                            state = tds.get(7).text();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        CardView st_view = view.findViewById(R.id.view);
                        TextView st_name = view.findViewById(R.id.st_name);
                        TextView st_plan = view.findViewById(R.id.st_plan);
                        TextView st_data = view.findViewById(R.id.st_data);
                        TextView st_state = view.findViewById(R.id.st_state);
                        TextView st_local = view.findViewById(R.id.st_local);
                        TextView st_which = view.findViewById(R.id.st_which);
                        TextView st_kind = view.findViewById(R.id.st_kind);
                        LinearLayout onClick = view.findViewById(R.id.onClick);

                        st_name.setText("考试科目：" + name);
                        st_plan.setText("考试安排：" + plan);
                        st_data.setText("考试日期：" + data);
                        st_state.setText("考试情况：" + state);
                        st_local.setText("考试地点：" + local);
                        st_which.setText("考试形式：" + which);

                        if (kind.contains("补考")) {
                            st_kind.setTextColor(Objects.requireNonNull(getActivity()).getResources().getColor(R.color.red));
                        }
                        st_kind.setText("考试种类：" + kind);

                        final String details = "考试科目：" + name
                                + "\n考试安排：" + plan
                                + "\n考试日期：" + data
                                + "\n考试地点：" + local
                                + "\n考试形式：" + which
                                + "\n考试情况：" + state
                                + "\n考试种类：" + kind;

                        //座次表链接
                        final String finalLocal_url = local_url;
                        onClick.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                AlertDialog dialog = new AlertDialog.Builder(getActivity())
                                        .setTitle(title)
                                        .setMessage(details)
                                        .setPositiveButton(R.string.know, null)
                                        .setNegativeButton(R.string.share_test, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                MyUtils.shareText(getActivity(), "给你看看我的考试噢" +
                                                        "!\n\n" + details + "\n\n数据来自：" + Url.AppDownUrl);
                                            }
                                        })
                                        .setNeutralButton("查看座次表", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                MyUtils.openUrl(getActivity(), finalLocal_url, true);
                                            }
                                        })
                                        .create();
                                dialog.setCanceledOnTouchOutside(false);
                                dialog.show();
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String error) {
                refresh.finishRefresh();
                ToastUtils.showShort(R.string.try_again);
            }
        });
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