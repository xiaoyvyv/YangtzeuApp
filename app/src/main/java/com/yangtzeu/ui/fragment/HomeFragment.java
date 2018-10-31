package com.yangtzeu.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import com.yangtzeu.R;
import com.yangtzeu.presenter.HomePresenter;
import com.yangtzeu.ui.activity.MainActivity;
import com.yangtzeu.ui.activity.base.BaseFragment;
import com.yangtzeu.ui.adapter.FragmentAdapter;
import com.yangtzeu.ui.view.HomeView;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

/**
 * Created by Administrator on 2018/3/6.
 */

public class HomeFragment extends BaseFragment implements HomeView {
    // 标志位，标志已经初始化完成。
    private boolean isPrepared = false;
    @SuppressLint("StaticFieldLeak")
    public static TabLayout tabLayout;
    private Toolbar toolbar;
    private View rootView;
    @SuppressLint("StaticFieldLeak")
    public static ViewPager mViewPager;
    private DrawerLayout drawer;
    private TextView holiday;
    private TextView temp;
    private TextView weather;
    private TextView city;
    private TextView study_time;
    private TextView week;
    private TextView pass;

    @SuppressLint("StaticFieldLeak")
    public static AppBarLayout app_bar;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        findViews();
        isPrepared = true;
        lazyLoad();
        return rootView;
    }

    @Override
    public void findViews() {
        toolbar = rootView.findViewById(R.id.toolbar);
        mViewPager = rootView.findViewById(R.id.viewPager);
        tabLayout = rootView.findViewById(R.id.tabLayout);
        app_bar = rootView.findViewById(R.id.app_bar);
        holiday = rootView.findViewById(R.id.holiday);
        drawer = ((MainActivity) Objects.requireNonNull(getActivity())).getDrawerLayout();
        temp = rootView.findViewById(R.id.temp);
        weather = rootView.findViewById(R.id.weather);
        city = rootView.findViewById(R.id.city);
        pass = rootView.findViewById(R.id.pass);
        week = rootView.findViewById(R.id.week);
        study_time=rootView.findViewById(R.id.study_time);
    }

    @Override
    public void setEvents() {


        HomePresenter presenter = new HomePresenter(getActivity(), this);
        presenter.setToolbarWhitDrawer();
        presenter.getHoliday();
        presenter.getWeather();
        presenter.setStudyTime();


        //设置Fragment适配器
        FragmentAdapter fragmentAdapter = new FragmentAdapter(Objects.requireNonNull(getActivity()).getSupportFragmentManager());
        Fragment fragment1 = new HomePartFragment1();
        fragmentAdapter.addFragment(getString(R.string.jwc_news),fragment1);

        Fragment fragment2 = new HomePartFragment2();
        fragment2.setUserVisibleHint(true);
        fragmentAdapter.addFragment(getString(R.string.school_news), fragment2);

        Fragment fragment3 = new HomePartFragment3();
        fragmentAdapter.addFragment(getString(R.string.school_lib), fragment3);

        mViewPager.setOffscreenPageLimit(2);
        mViewPager.setAdapter(fragmentAdapter);

        presenter.fitViewPagerAndTabLayout();

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


    @Override
    public Toolbar getToolbar() {
        return toolbar;
    }


    @Override
    public DrawerLayout getDrawerLayout() {
        return drawer;
    }

    @Override
    public ViewPager getViewPager() {
        return mViewPager;
    }

    @Override
    public TabLayout getTabLayout() {
        return tabLayout;
    }

    @Override
    public AppBarLayout getAppBarLayout() {
        return app_bar;
    }

    @Override
    public TextView getHoliday() {
        return holiday;
    }

    @Override
    public TextView getTemp() {
        return temp;
    }

    @Override
    public TextView getWeather() {
        return weather;
    }

    @Override
    public TextView getCity() {
        return city;
    }

    @Override
    public TextView getWeek() {
        return week;
    }

    @Override
    public TextView getPass() {
        return pass;
    }

    @Override
    public TextView getStudyTime() {
        return study_time;
    }
}
