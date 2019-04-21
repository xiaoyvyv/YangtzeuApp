package com.yangtzeu.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.Utils;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.lib.loading.LVBlock;
import com.yangtzeu.R;
import com.yangtzeu.presenter.AnswerLayout1Presenter;
import com.yangtzeu.ui.activity.AnswerListActivity;
import com.yangtzeu.ui.activity.base.BaseFragment;
import com.yangtzeu.ui.view.AnswerLayout1View;
import com.yangtzeu.url.Url;
import com.yangtzeu.utils.GoogleUtils;
import com.yangtzeu.utils.MyUtils;

import cn.bingoogolapple.bgabanner.BGABanner;

/**
 * Created by Administrator on 2018/3/6.
 */

public class AnswerFragment1 extends BaseFragment implements AnswerLayout1View, View.OnClickListener {
    // 标志位，标志已经初始化完成。
    public boolean isPrepared = false;
    public View rootView;
    private BGABanner banner;
    private LinearLayout container;
    private LVBlock loading;
    private LinearLayout googleView;
    private LinearLayout bottomViewContainer;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_answer_layout1, container, false);
        findViews();
        isPrepared = true;
        lazyLoad();
        return rootView;
    }

    @Override
    public void findViews() {
        banner = rootView.findViewById(R.id.banner);
        container = rootView.findViewById(R.id.slow_container);
        googleView = rootView.findViewById(R.id.googleView);
        loading = rootView.findViewById(R.id.loading);
        bottomViewContainer = rootView.findViewById(R.id.bottomViewContainer);
    }

    @Override
    public void setEvents() {
        rootView.findViewById(R.id.answer_yy).setOnClickListener(this);
        rootView.findViewById(R.id.answer_sx).setOnClickListener(this);
        rootView.findViewById(R.id.answer_wl).setOnClickListener(this);
        rootView.findViewById(R.id.answer_jk).setOnClickListener(this);
        rootView.findViewById(R.id.answer_cj).setOnClickListener(this);
        rootView.findViewById(R.id.answer_hx).setOnClickListener(this);
        rootView.findViewById(R.id.answer_zt).setOnClickListener(this);
        rootView.findViewById(R.id.answer_dl).setOnClickListener(this);

        AnswerLayout1Presenter presenter = new AnswerLayout1Presenter(getActivity(), this);
        presenter.loadBanner();
        presenter.loadHotAnswer();

        loading.startAnim();
        loading.setViewColor(Utils.getApp().getResources().getColor(R.color.colorPrimary));

        AdView adView1 = GoogleUtils.newBannerView(getActivity(), AdSize.LARGE_BANNER);
        adView1.loadAd(GoogleUtils.getRequest());
        AdView adView2 = GoogleUtils.newBannerView(getActivity(), AdSize.LARGE_BANNER);
        adView2.loadAd(GoogleUtils.getRequest());
        googleView.addView(adView1);
        googleView.addView(adView2);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getActivity(), AnswerListActivity.class);
        switch (v.getId()) {
            case R.id.answer_yy:
                intent.putExtra("from_url", Url.Yangtzeu_Answer_YY);
                break;
            case R.id.answer_sx:
                intent.putExtra("from_url", Url.Yangtzeu_Answer_SX);
                break;
            case R.id.answer_wl:
                intent.putExtra("from_url", Url.Yangtzeu_Answer_WL);
                break;
            case R.id.answer_jk:
                intent.putExtra("from_url", Url.Yangtzeu_Answer_JK);
                break;
            case R.id.answer_cj:
                intent.putExtra("from_url", Url.Yangtzeu_Answer_CJ);
                break;
            case R.id.answer_hx:
                intent.putExtra("from_url", Url.Yangtzeu_Answer_HX);
                break;
            case R.id.answer_zt:
                intent.putExtra("from_url", Url.Yangtzeu_Answer_ZT);
                break;
            case R.id.answer_dl:
                intent.putExtra("from_url", Url.Yangtzeu_Answer_DL);
                break;
        }
        MyUtils.startActivity(intent);
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
    public BGABanner getBanner() {
        return banner;
    }

    @Override
    public LinearLayout getContainer() {
        return container;
    }

    @Override
    public LinearLayout getBottomContainer() {
        return bottomViewContainer;
    }

}
