package com.yangtzeu.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lib.chat.adapter.ContactGroupAdapter;
import com.yangtzeu.R;
import com.yangtzeu.presenter.ChatPart3Presenter;
import com.yangtzeu.ui.activity.base.BaseFragment;
import com.yangtzeu.ui.view.ChatPartView3;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

/**
 * Created by Administrator on 2018/3/6.
 */

public class ChatFragment3 extends BaseFragment implements ChatPartView3 {
    // 标志位，标志已经初始化完成。
    public boolean isPrepared = false;
    public View rootView;
    @SuppressLint("StaticFieldLeak")
    public static ChatPart3Presenter presenter;
    public SwipeRefreshLayout refresh;
    public RecyclerView recyclerView;
    private ContactGroupAdapter contactAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_chat_part3, container, false);
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
        contactAdapter = new ContactGroupAdapter(getActivity());
        recyclerView.setAdapter(contactAdapter);

        presenter = new ChatPart3Presenter(getActivity(), this);
        presenter.addAdmin();

        refresh.setRefreshing(true);
        presenter.loadContacts();


        refresh.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
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
    public void onDestroy() {
        presenter = null;
        super.onDestroy();
    }

    @Override
    public SwipeRefreshLayout getRefresh() {
        if (refresh == null) {
            refresh = rootView.findViewById(R.id.refresh);
        }
        return refresh;
    }

    @Override
    public ContactGroupAdapter getAdapter() {
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
