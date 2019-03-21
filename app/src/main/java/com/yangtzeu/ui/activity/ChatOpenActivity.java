package com.yangtzeu.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.android.material.textfield.TextInputEditText;
import com.lib.chat.adapter.ChatAdapter;
import com.lib.chat.common.Constant;
import com.lib.chat.common.UserManager;
import com.yangtzeu.R;
import com.yangtzeu.presenter.ChatOpenPresenter;
import com.yangtzeu.ui.activity.base.BaseActivity;
import com.yangtzeu.ui.fragment.ChatFragment1;
import com.yangtzeu.ui.view.ChatOpenView;
import com.yangtzeu.utils.MyUtils;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class ChatOpenActivity extends BaseActivity implements ChatOpenView {
    private Toolbar toolbar;
    private ChatOpenPresenter presenter;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout refresh;
    private ChatAdapter chatAdapter;
    private String type;
    private String name;
    private String id;

    private String count = "10";
    private long end_time;
    private boolean isFresh = false;
    private TextInputEditText send_text;
    private AppCompatImageView send_btn;
    private AppCompatImageView send_image;
    private String bizType = Constant.TEXT;
    private String cardBackGround = "#ffffff";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        name = getIntent().getStringExtra("name");
        type = getIntent().getStringExtra("type");
        id = getIntent().getStringExtra("id");
        LogUtils.e(type,id);
        //设置会话全部已读
        UserManager.getInstance().setUnReadNone(id);

        setContentView(R.layout.activity_chat_open);
        init();
        MyUtils.setToolbarBackToHome(this, toolbar);

        if (type.equals(Constant.USER_TYPE_USER)) {
            toolbar.setTitle("用户：" + id);
        } else {
            if (StringUtils.isEmpty(name)) {
                toolbar.setTitle("群：" + id);
            } else {
                toolbar.setTitle(name);
            }
        }
    }

    @Override
    public void findViews() {
        toolbar = findViewById(R.id.toolbar);
        refresh = findViewById(R.id.refresh);
        recyclerView = findViewById(R.id.recyclerView);
        send_btn = findViewById(R.id.send_btn);
        send_image = findViewById(R.id.send_image);
        send_text = findViewById(R.id.send_text);
    }


    @Override
    public void setEvents() {
        end_time = TimeUtils.getNowMills();

        chatAdapter = new ChatAdapter(this);
        recyclerView.setAdapter(chatAdapter);

        presenter = new ChatOpenPresenter(this, this);
        presenter.setMessListener();


        presenter.loadHistoryMessage();
        refresh.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isFresh = true;
                presenter.loadHistoryMessage();
            }
        });

        //焦点变化时，自动滑到底部
        send_text.setOnFocusChangeListener(new View.
                OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                scrollBottom();
            }
        });

        send_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollBottom();
            }
        });

        //发送消息
        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isFresh = false;
                presenter.sendMessage();
            }
        });

        send_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showShort("图片系统开发中");
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("详情").setIcon(R.drawable.ic_details).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (type.equals(Constant.USER_TYPE_TOPIC)) {
                    //查看群信息
                    Intent intent = new Intent(ChatOpenActivity.this, ChatGroupInfoActivity.class);
                    intent.putExtra("topicId", id);
                    MyUtils.startActivity(intent);
                }
                return false;
            }
        }).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onDestroy() {
        //设置监听（ChatActivity）
        ChatFragment1.setMessListener();
        super.onDestroy();
    }

    @Override
    public SwipeRefreshLayout getRefresh() {
        return refresh;
    }

    @Override
    public ChatAdapter getAdapter() {
        return chatAdapter;
    }

    @Override
    public String getContactType() {
        return type;
    }

    @Override
    public String getContactId() {
        return id;
    }

    @Override
    public String getCount() {
        return count;
    }

    @Override
    public String getEndTime() {
        return String.valueOf(end_time);
    }

    @Override
    public void setEndTime(long end_time) {
        this.end_time = end_time;
    }

    @Override
    public void scrollBottom() {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!isFresh)
                    recyclerView.scrollToPosition(chatAdapter.getItemCount() - 1);
            }
        }, 100);
    }

    @Override
    public TextInputEditText getSendText() {
        return send_text;
    }

    @Override
    public String getBizType() {
        return bizType;
    }

    @Override
    public String getBackGround() {
        return cardBackGround;
    }

}
