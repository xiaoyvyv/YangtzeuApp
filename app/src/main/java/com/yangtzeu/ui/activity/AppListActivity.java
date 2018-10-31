package com.yangtzeu.ui.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.yangtzeu.R;
import com.yangtzeu.entity.AppBean;
import com.yangtzeu.http.OkHttp;
import com.yangtzeu.http.OnResultStringListener;
import com.yangtzeu.ui.activity.base.BaseActivity;
import com.yangtzeu.ui.adapter.AppListAdapter;
import com.yangtzeu.url.Url;
import com.yangtzeu.utils.MyUtils;
import com.yangtzeu.utils.PostUtils;


import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;


public class AppListActivity extends BaseActivity {
    private Toolbar toolbar;
    private RecyclerView mRecyclerView;
    private AppListAdapter appListAdapter;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_list);
        init();
        MyUtils.setToolbarBackToHome(this, toolbar);

    }

    @Override
    public void findViews() {
        toolbar = findViewById(R.id.toolbar);
        mRecyclerView = findViewById(R.id.mRecyclerView);

    }

    @Override
    public void setEvents() {
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        appListAdapter = new AppListAdapter(this);
        mRecyclerView.setAdapter(appListAdapter);

        progressDialog = MyUtils.getProgressDialog(AppListActivity.this, getString(R.string.loading));
        progressDialog.show();

        OkHttp.do_Get(Url.Yangtzeu_App_MyApp, new OnResultStringListener() {

            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                AppBean bean = gson.fromJson(response, AppBean.class);
                appListAdapter.setData(bean);
                appListAdapter.notifyItemRangeChanged(0, appListAdapter.getItemCount());
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(String error) {

                ToastUtils.showShort("加载失败");
                progressDialog.dismiss();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("App需求").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                final EditText edit = new EditText(AppListActivity.this);
                int dp20 = ConvertUtils.dp2px(25);
                edit.setPadding(dp20, dp20 / 2, dp20, 0);
                edit.setTextSize(15);
                edit.setHint("请输入校园软件需求建议");
                edit.setBackgroundColor(getResources().getColor(R.color.translate));
                AlertDialog dialog = new AlertDialog.Builder(AppListActivity.this)
                        .setTitle("软件需求")
                        .setMessage("请输入校园软件需求建议（或者您的软件需求-有偿）\n并附加上联系方式")
                        .setView(edit)
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                MyUtils.canCloseDialog(dialog, true);
                            }
                        })
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String s = edit.getText().toString().trim();
                                if (s.isEmpty()) {
                                    ToastUtils.showShort("未输入");
                                    return;
                                }
                                PostUtils.sendMessage("1223414335", "<h1>软件需求<h1>" + s);
                            }
                        }).create();
                dialog.show();
                dialog.setCanceledOnTouchOutside(false);
                dialog.setCancelable(false);
                return false;
            }
        }).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }

}

