package com.yangtzeu.ui.fragment;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.lib.calculator.calculator.CalculatorActivity;
import com.lib.notice.NoticeView;
import com.yangtzeu.R;
import com.yangtzeu.presenter.ManyPresenter;
import com.yangtzeu.ui.activity.ChatActivity;
import com.yangtzeu.ui.activity.CompassActivity;
import com.yangtzeu.ui.activity.KgActivity;
import com.yangtzeu.ui.activity.LockActivity;
import com.yangtzeu.ui.activity.LoveActivity;
import com.yangtzeu.ui.activity.RulerActivity;
import com.yangtzeu.ui.activity.ShopActivity;
import com.yangtzeu.ui.activity.TranslateActivity;
import com.yangtzeu.ui.activity.WebListActivity;
import com.yangtzeu.ui.activity.base.BaseFragment;
import com.yangtzeu.ui.view.ManyView;
import com.yangtzeu.url.Url;
import com.yangtzeu.utils.MyUtils;
import com.yangtzeu.utils.NotificationUtils;
import com.yangtzeu.utils.YangtzeuUtils;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import cn.bingoogolapple.bgabanner.BGABanner;

/**
 * Created by Administrator on 2018/3/6.
 */

@SuppressLint("SetTextI18n")
public class ManyFragment extends BaseFragment implements ManyView, View.OnClickListener {
    private RecyclerView mRecyclerView;
    // 标志位，标志已经初始化完成。
    private boolean isPrepared = false;
    private View rootView;
    private FragmentActivity activity;
    private ManyPresenter presenter;
    private NoticeView noticeView;
    private Toolbar toolbar;
    private SwipeRefreshLayout refresh;
    private BGABanner banner;
    private TextView tripLike;
    private TextView timesLike;
    private TextView tripLock;
    private TextView timesLock;
    private TextView tripQi;
    private TextView timesQi;
    private TextView tripWeb;
    private TextView timesWeb;
    private TextView tripRuler;
    private TextView timesRuler;
    private TextView tripCompass;
    private TextView timesCompass;
    private TextView tripTranslate;
    private TextView timesTranslate;
    private TextView tripKg;
    private TextView timesKg;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_many, container, false);
        findViews();
        isPrepared = true;
        lazyLoad();
        return rootView;
    }

    @Override
    public void findViews() {
        toolbar = rootView.findViewById(R.id.toolbar);
        noticeView = rootView.findViewById(R.id.noticeView);
        banner = rootView.findViewById(R.id.banner);
        mRecyclerView = rootView.findViewById(R.id.mRecyclerView);
        refresh = rootView.findViewById(R.id.refresh);

        rootView.findViewById(R.id.lock).setOnClickListener(this);
        rootView.findViewById(R.id.shop).setOnClickListener(this);
        rootView.findViewById(R.id.chat).setOnClickListener(this);
        rootView.findViewById(R.id.love).setOnClickListener(this);
        rootView.findViewById(R.id.cut_off).setOnClickListener(this);
        rootView.findViewById(R.id.lock).setOnClickListener(this);
        rootView.findViewById(R.id.like).setOnClickListener(this);
        rootView.findViewById(R.id.web).setOnClickListener(this);
        rootView.findViewById(R.id.calculator).setOnClickListener(this);
        rootView.findViewById(R.id.ruler).setOnClickListener(this);
        rootView.findViewById(R.id.compass).setOnClickListener(this);
        rootView.findViewById(R.id.translate).setOnClickListener(this);
        rootView.findViewById(R.id.kg).setOnClickListener(this);

        tripLike = rootView.findViewById(R.id.tripLike);
        timesLike = rootView.findViewById(R.id.timesLike);

        tripLock = rootView.findViewById(R.id.tripLock);
        timesLock = rootView.findViewById(R.id.timesLock);

        tripQi = rootView.findViewById(R.id.tripQi);
        timesQi = rootView.findViewById(R.id.timesQi);

        tripWeb = rootView.findViewById(R.id.tripWeb);
        timesWeb = rootView.findViewById(R.id.timesWeb);

        tripRuler = rootView.findViewById(R.id.tripRuler);
        timesRuler = rootView.findViewById(R.id.timesRuler);

        tripCompass = rootView.findViewById(R.id.tripCompass);
        timesCompass = rootView.findViewById(R.id.timesCompass);

        tripTranslate = rootView.findViewById(R.id.tripTranslate);
        timesTranslate = rootView.findViewById(R.id.timesTranslate);

        tripKg = rootView.findViewById(R.id.tripKg);
        timesKg = rootView.findViewById(R.id.timesKg);

    }

    @Override
    public void setEvents() {
        YangtzeuUtils.getOnClickTimes(tripLike, timesLike, false);
        YangtzeuUtils.getOnClickTimes(tripLock, timesLock, false);
        YangtzeuUtils.getOnClickTimes(tripQi, timesQi, false);
        YangtzeuUtils.getOnClickTimes(tripWeb, timesWeb, false);
        YangtzeuUtils.getOnClickTimes(tripRuler, timesRuler, false);
        YangtzeuUtils.getOnClickTimes(tripCompass, timesCompass, false);
        YangtzeuUtils.getOnClickTimes(tripTranslate, timesTranslate, false);
        YangtzeuUtils.getOnClickTimes(tripKg, timesKg, false);

        refresh.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        toolbar.inflateMenu(R.menu.fragment_many_menu);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.hide:
                        MyUtils.getAlert(getActivity(), getString(R.string.is_hide_many_page), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ToastUtils.showShort(R.string.hide_success);
                                SPUtils.getInstance("app_info").put("is_hide_many_page", true);
                            }
                        }).show();
                        break;
                }
                return false;
            }
        });

        activity = getActivity();
        presenter = new ManyPresenter(activity, this);
        presenter.loadMarqueeView();
        presenter.fitAdapter();
        presenter.loadBanner();


        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh.setRefreshing(false);
                presenter.loadMarqueeView();
                presenter.fitAdapter();

                //刷新统计
                YangtzeuUtils.getOnClickTimes(tripLike, timesLike, false);
                YangtzeuUtils.getOnClickTimes(tripLock, timesLock, false);
                YangtzeuUtils.getOnClickTimes(tripQi, timesQi, false);
                YangtzeuUtils.getOnClickTimes(tripWeb, timesWeb, false);
                YangtzeuUtils.getOnClickTimes(tripRuler, timesRuler, false);
                YangtzeuUtils.getOnClickTimes(tripCompass, timesCompass, false);
                YangtzeuUtils.getOnClickTimes(tripTranslate, timesTranslate, false);
                YangtzeuUtils.getOnClickTimes(tripKg, timesKg, false);


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
    public NoticeView getNoticeView() {
        return noticeView;
    }

    @Override
    public BGABanner getBGABanner() {
        return banner;
    }

    @Override
    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.shop:
                MyUtils.startActivity(ShopActivity.class);
                break;
            case R.id.chat:
                MyUtils.startActivity(ChatActivity.class);
                break;
            case R.id.love:
                MyUtils.startActivity(LoveActivity.class);
                break;
            case R.id.cut_off:
                MyUtils.openUrl(activity, Url.My_Home, true);
                break;
            case R.id.ruler:
                MyUtils.startActivity(RulerActivity.class);
                YangtzeuUtils.getOnClickTimes(tripRuler, timesRuler, true);
                break;
            case R.id.lock:
                boolean isEnable = NotificationUtils.isNotificationEnabled(activity);
                YangtzeuUtils.getOnClickTimes(tripLock, timesLock, true);
                if (isEnable) {
                    MyUtils.startActivity(LockActivity.class);
                } else {
                    MyUtils.getAlert(activity, "检测到您未允许本App的锁屏通知权限，建议您允许！\n\n请勾选允许锁屏通知\n\n权限用于在锁屏界面显示手机锁定剩余时间\n\n若不需要显示请点击取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            NotificationUtils.toNotificationSetting(activity);
                        }
                    }, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            MyUtils.startActivity(LockActivity.class);
                        }
                    }).show();
                }
                break;
            case R.id.like:
                YangtzeuUtils.getOnClickTimes(tripLike, timesLike, true);
                presenter.loadQQLikeEvents();
                break;
            case R.id.web:
                YangtzeuUtils.getOnClickTimes(tripWeb, timesWeb, true);
                MyUtils.startActivity(WebListActivity.class);
                break;
            case R.id.calculator:
                YangtzeuUtils.getOnClickTimes(tripQi, timesQi, true);
                MyUtils.startActivity(CalculatorActivity.class);
                break;
            case R.id.compass:
                YangtzeuUtils.getOnClickTimes(tripCompass, timesCompass, true);
                MyUtils.startActivity(CompassActivity.class);
                break;
            case R.id.translate:
                YangtzeuUtils.getOnClickTimes(tripTranslate, timesTranslate, true);
                MyUtils.startActivity(TranslateActivity.class);
                break;
            case R.id.kg:
                YangtzeuUtils.getOnClickTimes(tripKg, timesKg, true);
                MyUtils.startActivity(KgActivity.class);
                break;

        }
    }
}
