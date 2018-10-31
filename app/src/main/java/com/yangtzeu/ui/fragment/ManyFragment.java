package com.yangtzeu.ui.fragment;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.lib.notice.NoticeView;
import com.yangtzeu.R;
import com.yangtzeu.presenter.ManyPresenter;
import com.yangtzeu.ui.activity.ChatActivity;
import com.yangtzeu.ui.activity.LoveActivity;
import com.yangtzeu.ui.activity.ShopActivity;
import com.yangtzeu.ui.activity.base.BaseFragment;
import com.yangtzeu.ui.view.ManyView;
import com.yangtzeu.url.Url;
import com.yangtzeu.utils.MyUtils;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import cn.bingoogolapple.bgabanner.BGABanner;

/**
 * Created by Administrator on 2018/3/6.
 */

@SuppressLint("SetTextI18n")
public class ManyFragment extends BaseFragment implements ManyView, View.OnClickListener {
    private    RecyclerView mRecyclerView;
    // 标志位，标志已经初始化完成。
    private boolean isPrepared = false;
    private View rootView;
    private FragmentActivity activity;
    private ManyPresenter presenter;
    private NoticeView noticeView;
    private Toolbar toolbar;
    private BGABanner banner;

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
        rootView.findViewById(R.id.shop).setOnClickListener(this);
        rootView.findViewById(R.id.chat).setOnClickListener(this);
        rootView.findViewById(R.id.love).setOnClickListener(this);
        rootView.findViewById(R.id.cut_off).setOnClickListener(this);

        toolbar.inflateMenu(R.menu.fragment_many_menu);
        toolbar.setOnMenuItemClickListener(new  Toolbar.OnMenuItemClickListener() {
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
    }

    @Override
    public void setEvents() {
        activity = getActivity();
        presenter = new ManyPresenter(activity, this);
        presenter.loadMarqueeView();
        presenter.loadBanner();
        presenter.fitAdapter();

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
        }
    }
}
