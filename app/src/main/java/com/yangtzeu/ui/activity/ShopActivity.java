package com.yangtzeu.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.android.material.tabs.TabLayout;
import com.yangtzeu.R;
import com.yangtzeu.ui.activity.base.BaseActivity;
import com.yangtzeu.ui.adapter.FragmentAdapter;
import com.yangtzeu.ui.fragment.ShopFragment1;
import com.yangtzeu.utils.MyUtils;
import com.yangtzeu.utils.YangtzeuUtils;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

public class ShopActivity extends BaseActivity {
    public static String[] SHOP_KIND = {"新鲜", "手机", "数码", "服装", "居家", "美妆", "运动", "家电", "玩具乐器"};
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FragmentAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);
        init();
        MyUtils.setToolbarBackToHome(this, toolbar);

    }

    @Override
    public void findViews() {
        toolbar = findViewById(R.id.toolbar);
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);
    }

    @Override
    public void setEvents() {
        adapter = new FragmentAdapter(getSupportFragmentManager());
        for (String KIND : SHOP_KIND) {
            if (KIND.equals(SHOP_KIND[0])) {
                ShopFragment1 fragment = ShopFragment1.newInstance(null, null, false);
                adapter.addFragment(KIND, fragment);
            } else {
                ShopFragment1 fragment = ShopFragment1.newInstance("type", KIND, false);
                adapter.addFragment(KIND, fragment);
            }
        }
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(SHOP_KIND.length - 1);

        tabLayout.setupWithViewPager(viewPager);

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

        menu.add(R.string.my_shop).setIcon(R.drawable.ic_mine).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(ShopActivity.this, FragmentActivity.class);
                intent.putExtra("type", FragmentActivity.TYPE_SHOP);
                MyUtils.startActivity(intent);
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
                    YangtzeuUtils.inputInfoAlert(ShopActivity.this);
                } else {
                    MyUtils.startActivity(ShopAddActivity.class);
                }
                return true;
            }
        }).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }
}
