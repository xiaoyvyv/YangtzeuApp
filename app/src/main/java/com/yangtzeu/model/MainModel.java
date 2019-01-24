package com.yangtzeu.model;

import android.app.Activity;
import android.util.Log;
import android.view.MenuItem;

import com.blankj.utilcode.util.FragmentUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ServiceUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.LabelVisibilityMode;
import com.google.android.material.tabs.TabLayout;
import com.yangtzeu.R;
import com.yangtzeu.model.imodel.IMainModel;
import com.yangtzeu.service.BackgroundService;
import com.yangtzeu.ui.fragment.HomeFragment;
import com.yangtzeu.ui.view.MainView;
import com.yangtzeu.utils.YangtzeuUtils;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

public class MainModel implements IMainModel {
    @Override
    public void setBottomViewWithFragment(Activity activity, final MainView view) {
        final FragmentManager manager = ((AppCompatActivity) activity).getSupportFragmentManager();
        FragmentUtils.add(manager, view.getHomeFragment(), view.getFragmentContainer().getId(), false);


        boolean is_hide_many_page = SPUtils.getInstance("app_info").getBoolean("is_hide_many_page", false);
        if (!is_hide_many_page) {
            FragmentUtils.add(manager, view.getManyFragment(), view.getFragmentContainer().getId(), true);
        }

        FragmentUtils.add(manager, view.getTableFragment(), view.getFragmentContainer().getId(), true);
        FragmentUtils.add(manager, view.getMineFragment(), view.getFragmentContainer().getId(), true);
        FragmentUtils.add(manager, view.getGradeFragment(), view.getFragmentContainer().getId(), true);

        view.getBottomNavigationView().setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);
        view.getBottomNavigationView().setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.home:
                        view.getHomeFragment().setUserVisibleHint(true);
                        FragmentUtils.showHide(view.getHomeFragment(),
                                view.getManyFragment(), view.getGradeFragment(), view.getTableFragment(), view.getMineFragment());

                        TabLayout tabLayout = HomeFragment.tabLayout;
                        if (tabLayout != null) {
                            Objects.requireNonNull(tabLayout.getTabAt(0)).select();
                        }
                        return true;
                    case R.id.many:
                        view.getManyFragment().setUserVisibleHint(true);
                        FragmentUtils.showHide(view.getManyFragment(),
                                view.getGradeFragment(), view.getTableFragment(), view.getHomeFragment(), view.getMineFragment());

                        return true;
                    case R.id.grade:
                        view.getGradeFragment().setUserVisibleHint(true);
                        FragmentUtils.showHide(view.getGradeFragment(),
                                view.getManyFragment(), view.getTableFragment(), view.getHomeFragment(), view.getMineFragment());

                        return true;
                    case R.id.classTable:
                        view.getTableFragment().setUserVisibleHint(true);
                        FragmentUtils.showHide(view.getTableFragment(),
                                view.getManyFragment(), view.getGradeFragment(), view.getHomeFragment(), view.getMineFragment());
                        return true;
                    case R.id.mine:
                        view.getMineFragment().setUserVisibleHint(true);
                        FragmentUtils.showHide(view.getMineFragment(),
                                view.getManyFragment(), view.getGradeFragment(), view.getTableFragment(), view.getHomeFragment());
                        return true;
                }
                return false;
            }
        });

    }

    @Override
    public void initEvents(Activity activity, MainView view) {
        YangtzeuUtils.getTripInfo(activity, false);
        YangtzeuUtils.checkAppVersion(activity);
        ServiceUtils.startService(BackgroundService.class);
    }


}
