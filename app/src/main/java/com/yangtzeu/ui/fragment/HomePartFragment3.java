package com.yangtzeu.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.google.android.material.tabs.TabLayout;
import com.lib.x5web.WebViewProgressBar;
import com.lib.x5web.X5WebView;
import com.yangtzeu.R;
import com.yangtzeu.presenter.HomePart3Presenter;
import com.yangtzeu.ui.activity.base.BaseFragment;
import com.yangtzeu.ui.view.HomePartView3;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

/**
 * Created by Administrator on 2018/3/6.
 */

public class HomePartFragment3 extends BaseFragment implements HomePartView3 {
    // 标志位，标志已经初始化完成。
    private boolean isPrepared = false;
    private View rootView;
    @SuppressLint("StaticFieldLeak")
    public static TabLayout tabLayout;
    @SuppressLint("StaticFieldLeak")
    public static ViewPager mViewPager;
    @SuppressLint("StaticFieldLeak")
    public static X5WebView webView;
    private WebViewProgressBar progressView;
    private FrameLayout container;
    private Toolbar toolbar;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home_part3, container, false);
        findViews();
        isPrepared = true;
        lazyLoad();
        return rootView;
    }

    @Override
    public void findViews() {
        toolbar = rootView.findViewById(R.id.toolbar);
        mViewPager = rootView.findViewById(R.id.mViewPager);
        tabLayout = rootView.findViewById(R.id.tabLayout);
        progressView = rootView.findViewById(R.id.progressView);
        container = rootView.findViewById(R.id.slow_container);
    }

    @Override
    public void setEvents() {
        webView = new X5WebView(getActivity());
        HomePart3Presenter presenter = new HomePart3Presenter(getActivity(), this);
        presenter.loadView();
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
    public X5WebView getWebView() {
        return webView;
    }

    @Override
    public WebViewProgressBar getProgressBar() {
        return progressView;
    }

    @Override
    public Toolbar getToolbar() {
        return toolbar;
    }

    @Override
    public FrameLayout getContainer() {
        return container;
    }
}
