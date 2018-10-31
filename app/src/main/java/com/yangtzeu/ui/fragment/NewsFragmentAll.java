package com.yangtzeu.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.ToastUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yangtzeu.R;
import com.yangtzeu.entity.NewsBean;
import com.yangtzeu.presenter.NewsPresenter2;
import com.yangtzeu.ui.activity.base.BaseFragment;
import com.yangtzeu.ui.adapter.NewsAdapter;
import com.yangtzeu.ui.view.NewsView2;
import com.yangtzeu.url.Url;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Administrator on 2018/2/4.
 *
 */

public class NewsFragmentAll extends BaseFragment implements NewsView2 {
    // 标志位，标志已经初始化完成。
    private boolean isPrepared=false;
    private boolean isLoadFinish = false;
    private SmartRefreshLayout refresh;
    private RecyclerView recyclerView;
    private int page = 1;
    private String title;
    private NewsAdapter newsAdapter;
    private View rootView;
    private NewsPresenter2 presenter2;
    private String from_url;
    private String url;
    private List<NewsBean> beans = new ArrayList<>();

    public NewsFragmentAll() {

    }

    public static NewsFragmentAll newInstance(String url, String title) {
        NewsFragmentAll fragment = new NewsFragmentAll();
        Bundle bundle = new Bundle();
        bundle.putString("url", url);
        bundle.putString("title", title);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if (bundle != null) {
            from_url = bundle.getString("url");
            title = bundle.getString("title");
        }

        rootView =  inflater.inflate(R.layout.fragment_news_layout_all, container, false);
        findViews();
        isPrepared = true;
        lazyLoad();
        return rootView;
    }

    @Override
    public void findViews() {
        refresh = rootView.findViewById(R.id.refresh);
        recyclerView = rootView.findViewById(R.id.recyclerView);
    }

    @Override
    public void setEvents() {
        newsAdapter = new NewsAdapter(getActivity());
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(newsAdapter);
        presenter2 = new NewsPresenter2(getActivity(), this);

        refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(com.scwang.smartrefresh.layout.api.RefreshLayout refreshLayout) {
                page = 1;
                url= getNewsUrl(page);
                beans.clear();
                newsAdapter.clearData();
                presenter2.loadNewsData();
            }
        });

        refresh.setRefreshFooter(new ClassicsFooter(Objects.requireNonNull(getActivity())));
        refresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(com.scwang.smartrefresh.layout.api.RefreshLayout refreshLayout) {
                page = page + 1;
                url = getNewsUrl(page);
                presenter2.loadNewsData();
            }
        });
        refresh.autoRefresh();
    }

    @Override
    protected void lazyLoad() {
        if(!isPrepared || !isVisible) {
            return;
        }
        if (!isLoadFinish) {
            setEvents();
            isLoadFinish = true;
        }
    }

    @Override
    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    @Override
    public List<NewsBean> getData() {
        return beans;
    }





    @Override
    public NewsAdapter getNewsAdapter() {
        return newsAdapter;
    }

    @Override
    public String getKind() {
        return title;
    }

    @Override
    public SmartRefreshLayout getSmartRefreshLayout() {
        return refresh;
    }

    @Override
    public String getUrl() {
        return url;
    }


    private String getNewsUrl(int page) {
        String URL;
        if (from_url.contains("list")) {
            URL =  "http://news2.yangtzeu.edu.cn" + from_url + page + ".html";
        } else {
            URL = "http://news2.yangtzeu.edu.cn" + from_url;
        }
        return URL;
    }
}