package com.yangtzeu.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.yangtzeu.R;
import com.yangtzeu.presenter.HomePart2Presenter;
import com.yangtzeu.ui.activity.base.BaseFragment;
import com.yangtzeu.ui.view.HomePartView2;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

/**
 * Created by Administrator on 2018/3/6.
 */

public class HomePartFragment2 extends BaseFragment implements HomePartView2 {
    // 标志位，标志已经初始化完成。
    private boolean isPrepared = false;
    private View rootView;
    @SuppressLint("StaticFieldLeak")
    public static TabLayout tabLayout;
    @SuppressLint("StaticFieldLeak")
    public static ViewPager mViewPager;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home_part2, container, false);
        findViews();
        isPrepared = true;
        lazyLoad();
        return rootView;
    }

    @Override
    public void findViews() {
        mViewPager = rootView.findViewById(R.id.mViewPager);
        tabLayout = rootView.findViewById(R.id.tabLayout);
    }

    @Override
    public void setEvents() {
        HomePart2Presenter presenter = new HomePart2Presenter(getActivity(), this);
        presenter.fitView();
    }

    @Override
    protected void lazyLoad() {
        if (isLoadFinish) {
            return;
        }
        if (isPrepared) {
            setEvents();
            isLoadFinish = true;
        }
    }


    @Override
    public TabLayout getTabLayout() {
        return tabLayout;
    }

    @Override
    public ViewPager getViewPager() {
        return mViewPager;
    }
}
