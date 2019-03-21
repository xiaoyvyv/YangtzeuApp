package com.yangtzeu.ui.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.scwang.smartrefresh.header.DeliveryHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yangtzeu.R;
import com.yangtzeu.http.OkHttp;
import com.yangtzeu.http.OnResultStringListener;
import com.yangtzeu.ui.activity.PhysicalActivity;
import com.yangtzeu.ui.activity.base.BaseFragment;
import com.yangtzeu.url.Url;

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

public class PhysicalAddFragment extends BaseFragment {
    // 标志位，标志已经初始化完成。
    private boolean isPrepared = false;
    private boolean isLoadFinish = false;

    private View InflateView;
    private SmartRefreshLayout refresh;
    private LinearLayout list_container;
    private LinearLayout all_container;
    private String from_url;
    private String expday = "0";
    private String expclass = "0";
    private Spinner physical_day;
    private Spinner physical_which;


    public static PhysicalAddFragment newInstance(String from_url) {
        PhysicalAddFragment fragment = new PhysicalAddFragment();
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
        InflateView = inflater.inflate(R.layout.fragment_physical_add, container, false);
        findViews();
        isPrepared = true;
        lazyLoad();

        return InflateView;
    }


    @Override
    public void findViews() {
        list_container = InflateView.findViewById(R.id.list_container);
        all_container = InflateView.findViewById(R.id.all_container);
        refresh = InflateView.findViewById(R.id.refresh);
        physical_which = InflateView.findViewById(R.id.physical_which);
        physical_day = InflateView.findViewById(R.id.physical_day);


    }

    @Override
    public void setEvents() {

        refresh.setRefreshHeader(new DeliveryHeader(Objects.requireNonNull(getActivity())));
        refresh.setEnableLoadMore(false);
        refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                getData();
            }
        });
        refresh.autoRefresh();

        physical_day.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                expday = String.valueOf(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        physical_which.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                expclass =  String.valueOf(i);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void getData() {
        OkHttp.do_Get(from_url, new OnResultStringListener() {
            @Override
            public void onResponse(String response) {
                Document document = Jsoup.parse(response);
                if (response.contains("未登录")) {
                    AlertDialog dialog = new AlertDialog.Builder(getActivity())
                            .setTitle(R.string.trip)
                            .setMessage("登录过期，请重新登录")
                            .setPositiveButton(R.string.know, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    ((PhysicalActivity) Objects.requireNonNull(getActivity())).showLogin();
                                }
                            })
                            .create();
                    dialog.show();
                } else if (response.contains("实验预约学时已满")) {
                    AlertDialog dialog = new AlertDialog.Builder(getActivity())
                            .setTitle(R.string.trip)
                            .setMessage("实验预约学时已满，不能再添加预约！")
                            .setPositiveButton(R.string.know, null)
                            .create();
                    dialog.show();
                    dialog.setCanceledOnTouchOutside(false);
                } else {
                    fitView(document);
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

    @SuppressLint("SetTextI18n,InflateParams")
    private void fitView(Document document) {
        final Elements tds = document.select("table.table2 >tbody>tr>td");
        all_container.removeAllViews();
        for (int i = 0; i < tds.size(); i++) {
            final String id = tds.get(i).select("td>input").attr("value");
            if (ObjectUtils.isEmpty(id)) {
                continue;
            }
            View view = Objects.requireNonNull(getActivity()).getLayoutInflater().inflate(R.layout.fragment_physical_add_item, null);
            all_container.addView(view);
            TextView textView = view.findViewById(R.id.text);

            String text = tds.get(i).text();
            textView.setText(text);
            //查询列表
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    all_container.setVisibility(View.GONE);
                    all_container.removeAllViews();

                    RequestBody body = new FormBody.Builder()
                            .add("expchoice", id)
                            .add("expclass", expclass)
                            .add("expday", expday)
                            .build();
                    Request request = new Request.Builder()
                            .url(Url.Yangtzeu_Physical_List)
                            .post(body)
                            .build();
                    OkHttp.do_Post(request, new OnResultStringListener() {
                        @Override
                        public void onResponse(String response) {
                            list_container.removeAllViews();
                            list_container.setVisibility(View.VISIBLE);

                            Document document1 = Jsoup.parse(response);
                            Elements trs = document1.select("table.table>tbody>tr");

                            if (trs.size() == 2) {
                                AlertDialog dialog = new AlertDialog.Builder(getActivity())
                                        .setTitle("温馨提示")
                                        .setMessage("没有查询到可以预约的时间段,所有查询的预约时间段已经满员，请换个实验试试！")
                                        .setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                hideList();
                                            }
                                        })
                                        .create();
                                dialog.show();
                                dialog.setCanceledOnTouchOutside(false);
                                dialog.setCancelable(false);
                                return;
                            }
                            for (int j = 1; j < trs.size(); j++) {
                                if (trs.get(j).toString().contains("colspan")) {
                                    return;
                                }
                                View view = Objects.requireNonNull(getActivity()).getLayoutInflater().inflate(R.layout.fragment_physical_add_list_item, null);
                                list_container.addView(view);
                                TextView st_name = view.findViewById(R.id.st_name);
                                TextView st_choosed = view.findViewById(R.id.st_choosed);
                                Button st_add = view.findViewById(R.id.st_add);
                                TextView st_week = view.findViewById(R.id.st_week);
                                TextView st_day = view.findViewById(R.id.st_day);
                                TextView st_which = view.findViewById(R.id.st_which);

                                Elements tds = trs.get(j).select("tr>td");

                                String name = "";
                                String week = "";
                                String day = "";
                                String which = "";
                                String choosed = "";
                                String add = "";
                                String url = "";

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
                                    choosed = tds.get(4).text();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                try {
                                    add = tds.get(5).text();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                try {
                                    url = "http://10.10.16.16" + tds.get(5).select("td>a").attr("href");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                st_name.setText("实验名称：" + name);
                                st_week.setText("实验周次：" + week);
                                st_day.setText("实验星期：" + day);
                                st_which.setText("实验节次：" + which);
                                st_choosed.setText("预约人数：" + choosed);
                                st_add.setText(add);

                                final String finalUrl = url;
                                st_add.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        OkHttp.do_Get(finalUrl, new OnResultStringListener() {
                                            @Override
                                            public void onResponse(String response) {
                                                Document doc = Jsoup.parse(response);
                                                String error = doc.select("p.error").text().trim();
                                                String success = doc.select("p.success").text().trim();
                                                AlertDialog dialog = new AlertDialog.Builder(getActivity())
                                                        .setTitle("温馨提示")
                                                        .setMessage(error + success)
                                                        .setNegativeButton("重新选择",null)
                                                        .setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                                hideList();
                                                            }
                                                        })
                                                        .create();
                                                dialog.show();
                                                dialog.setCanceledOnTouchOutside(false);
                                                dialog.setCancelable(false);
                                                getData();

                                            }

                                            @Override
                                            public void onFailure(String error) {
                                                hideList();
                                                ToastUtils.showShort("预约失败，请重试");
                                            }
                                        });
                                    }
                                });
                            }
                        }

                        @Override
                        public void onFailure(String error) {
                            ToastUtils.showShort(R.string.load_error);
                        }
                    });

                }
            });
        }
    }

    public boolean hideList() {
        if (list_container.getVisibility() == View.VISIBLE) {
            list_container.setVisibility(View.GONE);
            list_container.removeAllViews();
            all_container.setVisibility(View.VISIBLE);
            getData();
            return true;
        }
        return false;
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