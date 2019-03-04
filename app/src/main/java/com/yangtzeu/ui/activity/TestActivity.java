package com.yangtzeu.ui.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.blankj.utilcode.util.SPUtils;
import com.google.android.material.tabs.TabLayout;
import com.yangtzeu.R;
import com.yangtzeu.listener.OnResultListener;
import com.yangtzeu.presenter.TestPresenter;
import com.yangtzeu.ui.activity.base.BaseActivity;
import com.yangtzeu.ui.view.TestView;
import com.yangtzeu.url.Url;
import com.yangtzeu.utils.MyUtils;
import com.yangtzeu.utils.YangtzeuUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class TestActivity extends BaseActivity implements TestView {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private FrameLayout container;
    private ProgressDialog dialog;
    private TestPresenter presenter;
    private FragmentManager fm;
    private List<Fragment> fragments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        init();
        MyUtils.setToolbarBackToHome(this, toolbar);

    }

    @Override
    public void findViews() {
        toolbar = findViewById(R.id.toolbar);
        tabLayout = findViewById(R.id.tabLayout);
        container = findViewById(R.id.slow_container);

    }

    @Override
    public void setEvents() {
        url = Url.Yangtzeu_My_Test1;

        String term_id = SPUtils.getInstance("user_info").getString("term_id", Url.Default_Term);
        SPUtils.getInstance("user_info").put("test_id", term_id);

        dialog = MyUtils.getProgressDialog(this, getString(R.string.test_loading));
        fm = getSupportFragmentManager();

        presenter = new TestPresenter(this, this);
        presenter.getTestInfo();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.test_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.choose_date:
                YangtzeuUtils.showChooseTerm(TestActivity.this, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SPUtils.getInstance("user_info").put("test_id", String.valueOf(which));
                        presenter.getTestInfo();
                    }
                });
                break;
            case R.id.change:
                YangtzeuUtils.showChooseModel(new OnResultListener<Integer>() {
                    @Override
                    public void onResult(Integer projectId) {
                        switch (projectId) {
                            case 1:
                                url = Url.Yangtzeu_My_Test1;
                                presenter.getTestInfo();
                                break;
                            case 2:
                                url = Url.Yangtzeu_My_Test2;
                                presenter.getTestInfo();
                                break;
                        }
                    }
                });
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Toolbar getToolbar() {
        return toolbar;
    }

    @Override
    public TabLayout getTabLayout() {
        return tabLayout;
    }

    @Override
    public FrameLayout getContainer() {
        return container;

    }

    @Override
    public FragmentManager getManager() {
        return fm;
    }

    @Override
    public List<Fragment> getFragments() {
        return fragments;
    }

    @Override
    public ProgressDialog getProgressDialog() {
        return dialog;
    }

    @Override
    public String getIndexUrl() {
        return null;
    }

    @Override
    public String getUrl() {
        return url;
    }
}
