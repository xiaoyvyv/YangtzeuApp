package com.yangtzeu.ui.activity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;

import com.blankj.utilcode.util.StringUtils;
import com.google.android.material.tabs.TabLayout;
import com.lib.chat.common.UserManager;
import com.yangtzeu.R;
import com.yangtzeu.app.MyApplication;
import com.yangtzeu.presenter.ChatPresenter;
import com.yangtzeu.ui.activity.base.BaseActivity;
import com.yangtzeu.ui.adapter.FragmentAdapter;
import com.yangtzeu.ui.fragment.ChatFragment1;
import com.yangtzeu.ui.fragment.ChatFragment2;
import com.yangtzeu.ui.fragment.ChatFragment3;
import com.yangtzeu.ui.view.ChatView;
import com.yangtzeu.utils.MyUtils;

import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

public class ChatActivity extends BaseActivity implements ChatView {
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewpager;
    private ChatPresenter presenter;
    private ChatFragment1 chatFragment1;
    private ChatFragment2 chatFragment2;
    private ChatFragment3 chatFragment3;


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
        tabLayout = findViewById(R.id.tabLayout);
        viewpager = findViewById(R.id.viewpager);
    }


    @Override
    public void setEvents() {
        presenter = new ChatPresenter(this, this);

        chatFragment1 = new ChatFragment1();
        chatFragment2 = new ChatFragment2();
        chatFragment3 = new ChatFragment3();

        FragmentManager manger = getSupportFragmentManager();

        FragmentAdapter adapter = new FragmentAdapter(manger);
        adapter.addFragment("消息", chatFragment1);
        adapter.addFragment("联系人", chatFragment2);
        adapter.addFragment("群组", chatFragment3);
        viewpager.setAdapter(adapter);
        viewpager.setOffscreenPageLimit(2);


        TabLayout.Tab tab1 = tabLayout.newTab();
        tab1.setText("消息");
        TabLayout.Tab tab2 = tabLayout.newTab();
        tab2.setText("联系人");
        TabLayout.Tab tab3 = tabLayout.newTab();
        tab3.setText("群组");

        tabLayout.addTab(tab1);
        tabLayout.addTab(tab2);
        tabLayout.addTab(tab3);

        tabLayout.setupWithViewPager(viewpager);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        menu.add(R.string.search).setIcon(R.drawable.ic_add_circle).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                PopupMenu popupMenu = new PopupMenu(ChatActivity.this, toolbar);
                popupMenu.setGravity(Gravity.END);
                if (StringUtils.equals(UserManager.getInstance().getAccount(), "201603246")) {
                    popupMenu.getMenu().add(0, 0, 0, getString(R.string.create_group));
                }
                popupMenu.getMenu().add(0, 2, 0, getString(R.string.query_chat));
                popupMenu.getMenu().add(0, 3, 0, getString(R.string.yangtzeu_group));
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case 0:
                                presenter.createGroup();
                                break;
                            case 2:
                                UserManager.showChatNumberDialog(ChatActivity.this);
                                break;
                            case 3:
                                presenter.loadYangtzeuGroupInfo();
                                break;
                        }
                        return false;
                    }
                });
                popupMenu.show();

                return false;
            }
        }).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }

    @Override

    protected void onDestroy() {
        //设置通用监听
        MyApplication.setMessListener();

        ChatFragment1.refresh = null;
        super.onDestroy();
    }

    @Override
    public ChatFragment1 getFragmentPart1() {
        return chatFragment1;
    }

    @Override
    public ChatFragment2 getFragmentPart2() {
        return chatFragment2;
    }

    @Override
    public ChatFragment3 getFragmentPart3() {
        return chatFragment3;
    }
}
