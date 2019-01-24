package com.yangtzeu.ui.activity;

import android.os.Bundle;

import com.blankj.utilcode.util.FragmentUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.yangtzeu.R;
import com.yangtzeu.ui.activity.base.BaseActivity;
import com.yangtzeu.ui.fragment.LoveFragment;
import com.yangtzeu.ui.fragment.ShopFragment1;
import com.yangtzeu.utils.MyUtils;

import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.FragmentManager;


public class FragmentActivity extends BaseActivity {
    public static final int TYPE_LOVE = 0;
    public static final int TYPE_SHOP = 1;

    private Toolbar toolbar;
    private CoordinatorLayout fragment_container;
    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        type = getIntent().getIntExtra("type", -1);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frg_manger);
        init();
        MyUtils.setToolbarBackToHome(this, toolbar);

    }

    @Override
    public void findViews() {
        toolbar = findViewById(R.id.toolbar);
        fragment_container = findViewById(R.id.fragment_container);

    }

    @Override
    public void setEvents() {
        if (type == TYPE_LOVE) {
            String number = SPUtils.getInstance("user_info").getString("number");
            LoveFragment fragment = LoveFragment.newInstance("master_id",number, true);
            fragment.setUserVisibleHint(true);
            final FragmentManager manager = getSupportFragmentManager();
            FragmentUtils.add(manager, fragment, fragment_container.getId(), false);
        } else if (type == TYPE_SHOP) {
            String number = SPUtils.getInstance("user_info").getString("number");
            ShopFragment1 fragment = ShopFragment1.newInstance("master_id", number, true);
            fragment.setUserVisibleHint(true);
            final FragmentManager manager = getSupportFragmentManager();
            FragmentUtils.add(manager, fragment, fragment_container.getId(), false);
        } else {
            ToastUtils.showShort(R.string.load_error);
        }
    }

}

