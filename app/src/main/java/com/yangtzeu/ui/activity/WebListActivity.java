package com.yangtzeu.ui.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.yangtzeu.R;
import com.yangtzeu.entity.WebBean;
import com.yangtzeu.http.OkHttp;
import com.yangtzeu.http.OnResultStringListener;
import com.yangtzeu.ui.activity.base.BaseActivity;
import com.yangtzeu.ui.adapter.WebListAdapter;
import com.yangtzeu.url.Url;
import com.yangtzeu.utils.MyUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;


public class WebListActivity extends BaseActivity {
    private Toolbar toolbar;
    private RecyclerView mRecyclerView;
    private WebListAdapter webListAdapter;
    private ProgressDialog progressDialog;
    private String from_url;
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        from_url = getIntent().getStringExtra("from_url");
        title = getIntent().getStringExtra("title");

        if (StringUtils.isEmpty(from_url)) {
            from_url = Url.Yangtzeu_All_Web;
        }
        if (StringUtils.isEmpty(title)) {
            title = getString(R.string.yangtzeu_web_list);
        }

        setContentView(R.layout.activity_list);
        init();
        MyUtils.setToolbarBackToHome(this, toolbar);
    }

    @Override
    public void findViews() {
        toolbar = findViewById(R.id.toolbar);
        mRecyclerView = findViewById(R.id.mRecyclerView);
    }

    @Override
    public void setEvents() {
        toolbar.setTitle(title);

        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        webListAdapter = new WebListAdapter(this);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setAdapter(webListAdapter);

        progressDialog = MyUtils.getProgressDialog(WebListActivity.this, getString(R.string.loading));
        progressDialog.show();

        getAllWeb();

    }

    private void getAllWeb() {
        OkHttp.do_Get(from_url, new OnResultStringListener() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                WebBean bean = gson.fromJson(response, WebBean.class);
                webListAdapter.setData(bean);
                webListAdapter.notifyItemRangeChanged(0, webListAdapter.getItemCount());
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(String error) {
                ToastUtils.showShort("加载失败");
                progressDialog.dismiss();
            }
        });
    }

    public void getXueYuanWeb() {
        OkHttp.do_Get(Url.Yangtzeu_XueYuanWeb, new OnResultStringListener() {
            @Override
            public void onResponse(String response) {
                Document document = Jsoup.parse(response);
                Elements elements = document.select("#vsb_content_6277_u71 a");
                List<WebBean.WebListBean> listBeans = new ArrayList<>();
                for (Element element : elements) {
                    WebBean.WebListBean bean = new WebBean.WebListBean();
                    String a = element.attr("href");
                    String text = element.text();

                    bean.setTitle(text);
                    bean.setUrl(a);
                    listBeans.add(bean);
                }
                WebBean bean = new WebBean();
                bean.setWebList(listBeans);

                webListAdapter.setData(bean);
                webListAdapter.notifyItemRangeChanged(0, webListAdapter.getItemCount());
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(String error) {
                ToastUtils.showShort("加载失败");
                progressDialog.dismiss();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //若不为空，则不加载菜单
        if (!from_url.equals(Url.Yangtzeu_All_Web)) {
            menu.add("帮助").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    MyUtils.getAlert(WebListActivity.this, "\n本资源来源于网络，请在下载后24小时内删除!\n\n需要指定软件请到【用户反馈】留言！", null)
                            .show();
                    return false;
                }
            }).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            return super.onCreateOptionsMenu(menu);
        }

        menu.add("常用网站").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                getAllWeb();
                return false;
            }
        }).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menu.add("学院网站").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                getXueYuanWeb();
                return false;
            }
        }).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }
}

