package com.yangtzeu.ui.fragment;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
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
public class ManyFragment extends BaseFragment implements ManyView{
    private RecyclerView mRecyclerView;
    // 标志位，标志已经初始化完成。
    private boolean isPrepared = false;
    private View rootView;
    private ManyPresenter presenter;
    private NoticeView noticeView;
    private Toolbar toolbar;
    private SwipeRefreshLayout refresh;
    private BGABanner banner;
    private BGABanner banner_item;


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
        banner_item = rootView.findViewById(R.id.banner_item);
        mRecyclerView = rootView.findViewById(R.id.mRecyclerView);
        refresh = rootView.findViewById(R.id.refresh);

    }

    @Override
    public void setEvents() {

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

        presenter = new ManyPresenter(getActivity(), this);
        presenter.loadMarqueeView();
        presenter.fitAdapter();
        presenter.loadBanner();
        presenter.loadBannerItem();


        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh.setRefreshing(false);
                presenter.loadBanner();
                presenter.loadMarqueeView();
                presenter.fitAdapter();

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
    public SwipeRefreshLayout getRefresh() {
        return refresh;
    }

    @Override
    public BGABanner getBGABannerItem() {
        return banner_item;
    }

}
