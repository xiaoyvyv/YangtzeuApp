package com.yangtzeu.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yangtzeu.R;
import com.yangtzeu.listener.HomePart1Listener;
import com.yangtzeu.presenter.HomePart1Presenter;
import com.yangtzeu.ui.activity.base.BaseFragment;
import com.yangtzeu.ui.view.HomePartView1;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

/**
 * Created by Administrator on 2018/3/6.
 */

public class HomePartFragment1 extends BaseFragment implements HomePartView1 {
    // 标志位，标志已经初始化完成。
    private boolean isPrepared=false;
    private View rootView;
    private TextView jwc_jwtz_t;
    private TextView jwc_bzsw_t;
    private TextView jwc_jwdt_t;
    private TextView jwc_jxjb_t;
    private TextView app_notice_title;
    private TextView app_notice_text;
    private CardView app_notice_layout;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home_part1, container, false);
        findViews();
        isPrepared = true;
        lazyLoad();
        return rootView;
    }

    @Override
    public void findViews() {

        jwc_jwtz_t = rootView.findViewById(R.id.jwc_jwtz_t);
        jwc_bzsw_t = rootView.findViewById(R.id.jwc_bzsw_t);
        jwc_jwdt_t = rootView.findViewById(R.id.jwc_jwdt_t);
        jwc_jxjb_t = rootView.findViewById(R.id.jwc_jxjb_t);

        app_notice_title = rootView.findViewById(R.id.app_notice_title);
        app_notice_text = rootView.findViewById(R.id.app_notice_text);
        app_notice_layout = rootView.findViewById(R.id.app_notice_layout);

    }

    @Override
    public void setEvents() {
        HomePart1Presenter presenter = new HomePart1Presenter(getActivity(), this);
        presenter.loadNotice();
        presenter.loadData();
        jwc_jwtz_t.setOnClickListener(new HomePart1Listener.OnListener1(getActivity()));
        jwc_bzsw_t.setOnClickListener(new HomePart1Listener.OnListener2(getActivity()));
        jwc_jwdt_t.setOnClickListener(new HomePart1Listener.OnListener3(getActivity()));
        jwc_jxjb_t.setOnClickListener(new HomePart1Listener.OnListener4(getActivity()));
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
    public View getRootView() {
        return rootView;
    }

    @Override
    public TextView getNoticeTitle() {
        return app_notice_title;
    }

    @Override
    public TextView getNoticeText() {
        return app_notice_text;
    }

    @Override
    public CardView getNoticeLayout() {
        return app_notice_layout;
    }
}
