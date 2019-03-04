package com.yangtzeu.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.lib.chat.adapter.GroupInnerUserAdapter;
import com.lib.subutil.ClipboardUtils;
import com.yangtzeu.R;
import com.yangtzeu.presenter.ChatGroupInfoPresenter;
import com.yangtzeu.ui.activity.base.BaseActivity;
import com.yangtzeu.ui.view.ChatGroupInfoView;
import com.yangtzeu.utils.MyUtils;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cn.bingoogolapple.bgabanner.BGABanner;

public class ChatGroupInfoActivity extends BaseActivity implements ChatGroupInfoView {
    private ChatGroupInfoPresenter presenter;
    private Toolbar toolbar;
    private BGABanner banner;
    private TextView name;
    private TextView number;
    private LinearLayout mineInfoLayout;
    private String topicId;
    private RecyclerView recyclerView;
    private GroupInnerUserAdapter adapter;
    private TextView invite;
    private Switch disturb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        topicId = getIntent().getStringExtra("topicId");
        setContentView(R.layout.activity_chat_group_info);
        init();
        MyUtils.setToolbarBackToHome(this, toolbar);
    }

    @Override
    public void findViews() {
        toolbar = findViewById(R.id.toolbar);
        banner = findViewById(R.id.banner);
        name = findViewById(R.id.name);
        number = findViewById(R.id.number);
        recyclerView = findViewById(R.id.recyclerView);
        invite = findViewById(R.id.invite);
        disturb = findViewById(R.id.disturb);
        mineInfoLayout = findViewById(R.id.mineInfoLayout);

    }


    @Override
    public void setEvents() {
        if (ObjectUtils.isEmpty(topicId)) {
            ToastUtils.showShort("群id载入错误");
            return;
        }
        boolean isHide = SPUtils.getInstance("group_notice").getBoolean(topicId, false);
        disturb.setChecked(isHide);
        disturb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SPUtils.getInstance("group_notice").put(topicId, isChecked);
            }
        });


        adapter = new GroupInnerUserAdapter(this);
        LinearLayoutManager manger = new LinearLayoutManager(this);
        manger.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(manger);
        recyclerView.setAdapter(adapter);

        presenter = new ChatGroupInfoPresenter(this, this);
        presenter.loadGroupInfo();


        number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardUtils.copyText(topicId);
                ToastUtils.showShort("群ID已复制");
            }
        });

        invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.inviteJoinGroup();
            }
        });
    }

    @Override
    public Toolbar getToolbar() {
        return toolbar;
    }

    @Override
    public BGABanner getBanner() {
        return banner;
    }

    @Override
    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    @Override
    public TextView getName() {
        return name;
    }

    @Override
    public TextView getNumber() {
        return number;
    }

    @Override
    public LinearLayout getMineInfoLayout() {
        return mineInfoLayout;
    }

    @Override
    public String getTopicId() {
        return topicId;
    }

    @Override
    public GroupInnerUserAdapter getAdapter() {
        return adapter;
    }

}
