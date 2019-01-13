package com.yangtzeu.ui.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.lib.mob.chat.ChatUser;
import com.mob.imsdk.MobIM;
import com.mob.imsdk.MobIMCallback;
import com.mob.imsdk.model.IMMessage;
import com.yangtzeu.R;
import com.yangtzeu.presenter.ChatDetailsPresenter;
import com.yangtzeu.ui.activity.base.BaseActivity;
import com.yangtzeu.ui.adapter.ChatViewAdapter;
import com.yangtzeu.ui.view.ChatDetailsView;
import com.yangtzeu.url.Url;
import com.yangtzeu.utils.MyUtils;

import java.util.Collections;
import java.util.List;

import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;


public class ChatDetailsActivity extends BaseActivity implements ChatDetailsView, View.OnClickListener {
    public static final int MESSAGE_SIZE = 30;
    private Toolbar toolbar;
    private ChatDetailsPresenter president;
    private RecyclerView chatView;
    private String from_id;
    private int page = 1;
    private ChatViewAdapter adapter;
    private int type;
    private ChatUser[] chatUser = new ChatUser[2];
    private EditText input;
    private Button sendBt;
    private ImageView chat_bg;
    private NestedScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        from_id = getIntent().getStringExtra("id");
        type = getIntent().getIntExtra("type", 0);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_details);
        init();
        MyUtils.setToolbarBackToHome(this, toolbar);

        if (from_id.equals("10000005")) {
            toolbar.setTitle("新长大助手官方群");
        } else {
            toolbar.setTitle("用户：" + from_id);
        }
    }

    @Override
    public void findViews() {
        toolbar = findViewById(R.id.toolbar);
        chatView = findViewById(R.id.chatView);
        sendBt = findViewById(R.id.send_message);
        input = findViewById(R.id.input);
        scrollView = findViewById(R.id.scrollView);
        chat_bg = findViewById(R.id.chat_bg);
    }

    @Override
    public void setEvents() {
        Glide.with(this).load(Url.Yangtzeu_Chat_Background).into(chat_bg);
        sendBt.setOnClickListener(this);
        adapter = new ChatViewAdapter(this);
        president = new ChatDetailsPresenter(this, this);
        president.setUserInfo();
        president.setChatViewParam();
        president.addMessageReceiver();

        MobIM.getChatManager().getMessageList(from_id, type, MESSAGE_SIZE, page, new MobIMCallback<List<IMMessage>>() {
            @Override
            public void onSuccess(List<IMMessage> imMessages) {
                Collections.reverse(imMessages);
                for (int i = 0; i < imMessages.size(); i++) {
                    president.addMsgToChatView(imMessages.get(i));
                }
            }
            @Override
            public void onError(int i, String s) {
                LogUtils.e("查询用户历史消息失败", i, s);
            }
        });



    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.send_message:
                president.sendMessage();
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("添加").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                ToastUtils.showShort(R.string.making);
                return false;
            }
        }).setIcon(R.drawable.ic_add_circle).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public String getFromId() {
        return from_id;
    }

    @Override
    public ChatUser[] getChatUser() {
        return chatUser;
    }

    @Override
    public ChatViewAdapter getChatAdapter() {
        return adapter;
    }

    @Override
    public RecyclerView getChatView() {
        return chatView;
    }

    @Override
    public EditText getInputView() {
        return input;
    }

    @Override
    public Button getSendButton() {
        return sendBt;
    }

    @Override
    public NestedScrollView getNestedScrollView() {
        return scrollView;
    }

    @Override
    public int getType() {
        return type;
    }


    @Override
    protected void onDestroy() {
        president.removeMessageReceiver();
        super.onDestroy();
    }

}
