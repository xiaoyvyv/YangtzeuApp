package com.yangtzeu.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lib.chat.adapter.ContactUserAdapter;
import com.yangtzeu.R;
import com.yangtzeu.presenter.ChatPart2Presenter;
import com.yangtzeu.ui.activity.base.BaseFragment;
import com.yangtzeu.ui.view.ChatPartView2;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

/**
 * Created by Administrator on 2018/3/6.
 */

public class ChatFragment2 extends BaseFragment implements ChatPartView2 {
    // 标志位，标志已经初始化完成。
    public boolean isPrepared = false;
    public View rootView;
    private ChatPart2Presenter presenter;
    public  SwipeRefreshLayout refresh;
    public RecyclerView recyclerView;
    private ContactUserAdapter contactAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_chat_part2, container, false);
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
        contactAdapter = new ContactUserAdapter(getActivity());
        recyclerView.setAdapter(contactAdapter);

        presenter = new ChatPart2Presenter(getActivity(), this);
        presenter.addAdmin();

        refresh.setRefreshing(true);
        presenter.loadContacts();
        refresh.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                contactAdapter.clear();
                presenter.loadContacts();
            }
        });
    }

    @Override
    public void onResume() {
        if (presenter != null) {
            presenter.loadContacts();
        }
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
    public ContactUserAdapter getAdapter() {
        return contactAdapter;
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
