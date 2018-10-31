package com.yangtzeu.ui.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.google.android.material.textfield.TextInputEditText;
import com.mob.imsdk.model.IMConversation;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yangtzeu.R;
import com.yangtzeu.presenter.ChatPresenter;
import com.yangtzeu.ui.activity.base.BaseActivity;
import com.yangtzeu.ui.adapter.ConversationAdapter;
import com.yangtzeu.ui.view.ChatView;
import com.yangtzeu.utils.MyUtils;

import java.util.Objects;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;


public class ChatActivity extends BaseActivity implements ChatView {

    private Toolbar toolbar;
    private SmartRefreshLayout refresh;
    private ChatPresenter presenter;
    private RecyclerView recyclerView;
    private TextView mob_connect;
    private ConversationAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        init();
        MyUtils.setToolbarBackToHome(this, toolbar);
    }

    @Override
    public void findViews() {
        toolbar = findViewById(R.id.toolbar);
        refresh = findViewById(R.id.refresh);
        recyclerView = findViewById(R.id.recyclerView);
        mob_connect = findViewById(R.id.mob_connect);
    }

    @Override
    public void setEvents() {
        adapter = new ConversationAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(false);

        presenter = new ChatPresenter(this, this);

        presenter.addMessageReceiver();
        presenter.addGeneralReceiver();


        refresh.setEnableLoadMore(false);
        refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                refresh.finishRefresh();
                presenter.getAllLocalConversations();
            }
        });
        refresh.autoRefresh();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("添加").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                @SuppressLint("InflateParams")
                View view = getLayoutInflater().inflate(R.layout.view_input_chat, null);
                final TextInputEditText text = view.findViewById(R.id.text);
                final TextView trip = view.findViewById(R.id.trip);
                trip.setText("查询学号会话");
                AlertDialog dialog = new AlertDialog.Builder(ChatActivity.this)
                        .setView(view)
                        .setPositiveButton("开始聊天", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String id = Objects.requireNonNull(text.getText()).toString().trim();
                                if (MyUtils.isNumeric(id)) {
                                    //targetId - 目标id（群聊为群的id，私聊为对方id）
                                    Intent intent = new Intent(ChatActivity.this, ChatDetailsActivity.class);
                                    intent.putExtra("id", id);
                                    intent.putExtra("type", IMConversation.TYPE_USER);
                                    MyUtils.startActivity(intent);
                                } else {
                                    ToastUtils.showShort(R.string.input_number);
                                }
                            }
                        }).setNegativeButton(R.string.clear, null).create();
                dialog.show();
                return false;
            }
        }).setIcon(R.drawable.ic_add_circle).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public SmartRefreshLayout getRefresh() {
        return refresh;
    }

    @Override
    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    @Override
    public ConversationAdapter getAdapter() {
        return adapter;
    }

    @Override
    public TextView getConnectTrip() {
        return mob_connect;
    }

    @Override
    protected void onDestroy() {
        presenter.removeMessageReceiver();
        presenter.removeGeneralReceiver();
        super.onDestroy();
    }
}
