package com.yangtzeu.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.yangtzeu.R;
import com.yangtzeu.presenter.GradePresenter;
import com.yangtzeu.ui.activity.base.BaseFragment;
import com.yangtzeu.ui.adapter.FragmentAdapter;
import com.yangtzeu.ui.view.GradeView;
import com.yangtzeu.url.Url;
import com.yangtzeu.utils.MyUtils;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

/**
 * Created by Administrator on 2018/3/6.
 */

public class GradeFragment extends BaseFragment implements GradeView {
    // 标志位，标志已经初始化完成。
    public boolean isPrepared = false;
    public View rootView;
    private ViewPager pager;
    private TabLayout tab;
    private FragmentAdapter fragmentAdapter;
    @SuppressLint("StaticFieldLeak")
    public static Toolbar toolbar;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_grade, container, false);
        findViews();
        isPrepared = true;
        lazyLoad();
        return rootView;
    }

    @Override
    public void findViews() {
        toolbar = rootView.findViewById(R.id.toolbar);
        pager = rootView.findViewById(R.id.pager);
        tab = rootView.findViewById(R.id.tab);
    }

    @Override
    public void setEvents() {

        //设置Fragment适配器
        fragmentAdapter = new FragmentAdapter(Objects.requireNonNull(getActivity()).getSupportFragmentManager());

        final GradePresenter presenter = new GradePresenter(getActivity(), this);
        presenter.setViewPager();


        toolbar.inflateMenu(R.menu.grade_menu);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.web_grade:
                        MyUtils.openUrl(Objects.requireNonNull(getActivity()), Url.Yangtzeu_AllGrade_Url1);
                        break;
                }
                return false;
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


    @Override
    public FragmentAdapter getAdapter() {
        return fragmentAdapter;
    }

    @Override
    public ViewPager getViewPager() {
        return pager;
    }

    @Override
    public TabLayout getTabLayout() {
        return tab;
    }

    @Override
    public Toolbar getToolbar() {
        return toolbar;
    }

}
