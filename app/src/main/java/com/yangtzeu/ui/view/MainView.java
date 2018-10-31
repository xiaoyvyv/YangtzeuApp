package com.yangtzeu.ui.view;

import android.widget.FrameLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.yangtzeu.ui.fragment.GradeFragment;
import com.yangtzeu.ui.fragment.HomeFragment;
import com.yangtzeu.ui.fragment.ManyFragment;
import com.yangtzeu.ui.fragment.MineFragment;
import com.yangtzeu.ui.fragment.TableFragment;

import androidx.drawerlayout.widget.DrawerLayout;

public interface MainView {
    FrameLayout getFragmentContainer();
    HomeFragment getHomeFragment();
    TableFragment getTableFragment();

    ManyFragment getManyFragment();
    MineFragment getMineFragment();
    DrawerLayout getDrawerLayout();
    BottomNavigationView getBottomNavigationView();

    GradeFragment getGradeFragment();
}
