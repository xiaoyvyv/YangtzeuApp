package com.yangtzeu.ui.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.lib.chat.common.Constant;
import com.lib.subutil.GsonUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yangtzeu.R;
import com.yangtzeu.entity.BoardBean;
import com.yangtzeu.entity.MessageBean;
import com.yangtzeu.http.OkHttp;
import com.yangtzeu.http.OnResultStringListener;
import com.yangtzeu.presenter.BoardPresenter;
import com.yangtzeu.ui.activity.base.BaseActivity;
import com.yangtzeu.ui.adapter.BoardAdapter;
import com.yangtzeu.ui.view.BoardView;
import com.yangtzeu.url.Url;
import com.yangtzeu.utils.MyUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;


public class BoardActivity extends BaseActivity implements BoardView {
    private List<BoardBean.ResultBean> data = new ArrayList<>();
    private Toolbar toolbar;
    private BoardAdapter adapter;
    private RecyclerView mRecyclerView;
    private SmartRefreshLayout refresh;
    private BoardPresenter president;
    private int page = 0;
    private LinearLayout replay_container;
    private TextView replay_text;
    private Button send_replay;
    private TextView replay_title;
    private boolean isRefresh = true;
    private Button chat_online;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);
        init();
        MyUtils.setToolbarBackToHome(this, toolbar);
    }

    @Override
    public void findViews() {
        toolbar = findViewById(R.id.toolbar);
        mRecyclerView = findViewById(R.id.mRecyclerView);
        refresh = findViewById(R.id.refresh);

        replay_title = findViewById(R.id.replay_title);
        replay_container = findViewById(R.id.replay_container);
        send_replay = findViewById(R.id.send_replay);
        replay_text = findViewById(R.id.replay_text);
        chat_online = findViewById(R.id.chat_online);
    }

    @Override
    public void setEvents() {
        president = new BoardPresenter(this, this);
        adapter = new BoardAdapter(this);
        mRecyclerView.setAdapter(adapter);

        refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                isRefresh = true;
                page = 0;
                president.loadBoardData();
            }
        });
        refresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                isRefresh = false;
                page = page + 30;
                president.loadBoardData();
            }
        });
        refresh.autoRefresh();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("发表留言").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                MyUtils.startActivity(BoardAddActivity.class);
                return false;
            }
        }).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return true;
    }


    @SuppressLint("SetTextI18n")
    public void showReplayOrAdd(final String to_number, final String id, String title) {
        chat_online.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyUtils.chatOnline(BoardActivity.this,to_number, Constant.USER_TYPE_USER);
            }
        });

        replay_title.setText("回复：" + title);
        replay_container.setVisibility(View.VISIBLE);
        send_replay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = replay_text.getText().toString().trim();
                if (text.isEmpty()) {
                    ToastUtils.showShort(R.string.please_input);
                    return;
                }
                ToastUtils.showShort(R.string.replay_ing);
                KeyboardUtils.hideSoftInput(replay_text);
                replay_container.setVisibility(View.GONE);

                replay_text.setText(null);
                send_replay.setEnabled(false);

                String name = SPUtils.getInstance("user_info").getString("name");
                String footer = "?p_id=" + id + "&text=" + text + "&name=" + name;
                String url = Url.Yangtzeu_App_Reply_Message + footer;

                OkHttp.do_Get(url, new OnResultStringListener() {
                    @Override
                    public void onResponse(String response) {
                        replay_container.setVisibility(View.GONE);
                        send_replay.setEnabled(true);
                        if (response.isEmpty()) {
                            ToastUtils.showShort(R.string.replay_success_refresh);
                        } else {
                            ToastUtils.showShort(response);
                        }
                    }

                    @Override
                    public void onFailure(String error) {
                        send_replay.setEnabled(true);
                        ToastUtils.showShort(R.string.replay_error);
                    }
                });

                String from_number = SPUtils.getInstance("user_info").getString("number");
                String sen_message = Url.getSendMessage("留言板用户："+name+"-回复你内容：" + text, name,from_number, to_number);
                OkHttp.do_Get(sen_message, new OnResultStringListener() {
                    @Override
                    public void onResponse(String response) {
                        MessageBean bean1 = GsonUtils.fromJson(response, MessageBean.class);
                        if (bean1.getInfo().contains("留言成功")) {
                            ToastUtils.showShort(R.string.replay_success_trip);
                        } else {
                            ToastUtils.showShort(R.string.replay__trip);
                        }
                    }

                    @Override
                    public void onFailure(String error) {
                        LogUtils.i("回复消息发送失败，对方将不会收到提醒");
                    }
                });


            }
        });
    }

    @Override
    public boolean isRefresh() {
        return isRefresh;
    }

    @Override
    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    @Override
    public int getPage() {
        return page;
    }

    @Override
    public void setPage(int page) {
        this.page = page;
    }

    @Override
    public List<BoardBean.ResultBean> getBoardData() {
        return data;
    }


    @Override
    public BoardAdapter getAdapter() {
        return adapter;
    }

    @Override
    public SmartRefreshLayout getRefresh() {
        return refresh;
    }

    @Override
    public void onBackPressed() {
        if (replay_container.getVisibility() == View.VISIBLE) {
            replay_container.setVisibility(View.GONE);
        } else {
            super.onBackPressed();
        }
    }

    public LinearLayout getReplay_container() {
        return replay_container;
    }
}
