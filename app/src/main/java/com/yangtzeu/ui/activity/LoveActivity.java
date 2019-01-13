package com.yangtzeu.ui.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.yangtzeu.R;
import com.yangtzeu.ui.activity.base.BaseActivity;
import com.yangtzeu.ui.adapter.FragmentAdapter;
import com.yangtzeu.ui.fragment.LoveFragment;
import com.yangtzeu.url.Url;
import com.yangtzeu.utils.MediaPlayUtil;
import com.yangtzeu.utils.MyUtils;
import com.yangtzeu.utils.YangtzeuUtils;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

public class LoveActivity extends BaseActivity {
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FragmentAdapter adapter;
    private ImageView love_bg;
    private FloatingActionButton stop;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_love);
        init();
        MyUtils.setToolbarBackToHome(this, toolbar);
    }

    @Override
    public void findViews() {
        toolbar = findViewById(R.id.toolbar);
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);
        love_bg = findViewById(R.id.love_bg);
        stop = findViewById(R.id.stop);
    }

    @Override
    public void setEvents() {
        Glide.with(this).load(Url.Yangtzeu_App_Love).into(love_bg);

        adapter = new FragmentAdapter(getSupportFragmentManager());

        LoveFragment fragment1 = LoveFragment.newInstance(null, null, false);
        String number = SPUtils.getInstance("user_info").getString("number");
        LoveFragment fragment2 = LoveFragment.newInstance("master_id", number, true);
        adapter.addFragment("首页", fragment1);
        adapter.addFragment("我的", fragment2);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(1);
        tabLayout.setupWithViewPager(viewPager);

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stop.hide();
                MediaPlayUtil.getInstance().stop();
            }
        });
    }

    @Override
    protected void onResume() {
        boolean is_playing = MediaPlayUtil.getInstance().isPlaying();
        if (is_playing) {
            stop.show();
        }
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(R.string.search).setIcon(R.drawable.ic_search).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                ToastUtils.showShort("开发中");
                return true;
            }
        }).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        menu.add(R.string.add).setIcon(R.drawable.ic_add).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                String wechat = SPUtils.getInstance("user_info").getString("wechat");
                String qq = SPUtils.getInstance("user_info").getString("qq");
                String phone = SPUtils.getInstance("user_info").getString("phone");
                if (wechat.isEmpty() || qq.isEmpty() || phone.isEmpty()) {
                    YangtzeuUtils.inputInfoAlert(LoveActivity.this);

                } else {
                    MyUtils.startActivity(LoveAddActivity.class);
                }
                return true;
            }
        }).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }
}
