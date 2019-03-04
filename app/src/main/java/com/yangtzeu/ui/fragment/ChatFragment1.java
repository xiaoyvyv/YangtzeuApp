package com.yangtzeu.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.LogUtils;
import com.lib.chat.adapter.ContactAdapter;
import com.yangtzeu.R;
import com.yangtzeu.presenter.ChatPart1Presenter;
import com.yangtzeu.ui.activity.base.BaseFragment;
import com.yangtzeu.ui.view.ChatPartView1;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

/**
 * Created by Administrator on 2018/3/6.
 */

public class ChatFragment1 extends BaseFragment implements ChatPartView1 {
    // 标志位，标志已经初始化完成。
    public boolean isPrepared = false;
    public View rootView;

    @SuppressLint("StaticFieldLeak")
    private static ChatPart1Presenter presenter;
    @SuppressLint("StaticFieldLeak")
    public static SwipeRefreshLayout refresh;
    public RecyclerView recyclerView;
    private ContactAdapter contactAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_chat_part1, container, false);
        findViews();
        isPrepared = true;
        lazyLoad();
        return rootView;
    }


    @Override
    public void findViews() {
        refresh = rootView.findViewById(R.id.refresh);
        recyclerView = rootView.findViewById(R.id.recyclerView);
    }


    @Override
    public void setEvents() {
        contactAdapter = new ContactAdapter(getActivity());
        recyclerView.setAdapter(contactAdapter);

        presenter = new ChatPart1Presenter(getActivity(), this);
        presenter.setMessageListener();

        refresh.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.loadMessageHistory();
            }
        });
    }

    @Override
    public void onResume() {
        if (presenter != null)
            presenter.loadMessageHistory();
        super.onResume();
    }

    @Override
    public SwipeRefreshLayout getRefresh() {
        if (refresh == null) {
            refresh = rootView.findViewById(R.id.refresh);
        }
        return refresh;
    }

    @Override
    public ContactAdapter getAdapter() {
        return contactAdapter;
    }

    public static void setMessListener() {
        if (presenter != null)
            presenter.setMessageListener();
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


}
