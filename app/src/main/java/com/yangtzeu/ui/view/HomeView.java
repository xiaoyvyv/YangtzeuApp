package com.yangtzeu.ui.view;

import android.widget.TextView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

public interface HomeView {
    Toolbar getToolbar();

    DrawerLayout getDrawerLayout();

    ViewPager getViewPager();

    TabLayout getTabLayout();

    AppBarLayout getAppBarLayout();

    TextView getHoliday();

    TextView getTemp();

    TextView getWeather();

    TextView getCity();

    TextView getWeek();

    TextView getPass();

    TextView getStudyTime();

}
