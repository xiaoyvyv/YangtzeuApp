package com.yangtzeu.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.blankj.utilcode.util.ConvertUtils;
import com.yangtzeu.R;
import com.yangtzeu.entity.MatrixBean;
import com.yangtzeu.ui.activity.base.BaseActivity;
import com.yangtzeu.ui.adapter.MatrixAddAdapter;
import com.yangtzeu.utils.MyUtils;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MatrixAddActivity extends BaseActivity {
    private Toolbar toolbar;
    private MatrixBean matrix;
    private String name;
    private MatrixAddAdapter matrixAddAdapter;
    private int width;
    private int height;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        name = getIntent().getStringExtra("name");
        width = getIntent().getIntExtra("width", 3);
        height = getIntent().getIntExtra("height", 3);
        setContentView(R.layout.activity_matrix_add);
        init();
        MyUtils.setToolbarBackToHome(this,toolbar);
    }
    @Override
    public void findViews() {
        toolbar =  findViewById(R.id.toolbar);
        mRecyclerView =  findViewById(R.id.mRecyclerView);
    }

    @Override
    public void setEvents() {
        matrix = new MatrixBean();
        matrix.setName(name);
        matrix.setWidth(width);
        matrix.setHeight(height);

        matrixAddAdapter = new MatrixAddAdapter(MatrixAddActivity.this);
        matrixAddAdapter.setAdapterSize(width, height);
        GridLayoutManager manager = new GridLayoutManager(MatrixAddActivity.this, width);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                int mSpace = ConvertUtils.dp2px(5);
                outRect.left = mSpace;
                outRect.right = mSpace;
                outRect.bottom = mSpace;
                outRect.top = mSpace;
            }
        });
        mRecyclerView.setAdapter(matrixAddAdapter);
    }

    @SuppressLint("InflateParams,SetTextI18n")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("确定").setIcon(R.drawable.ic_check).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                sendMatrix();
                return false;
                }
        }).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }

    private void sendMatrix() {
        double[][] list = matrixAddAdapter.getEditTexts();
        matrix.setMatrix(list);

        Intent intent = getIntent();
        intent.putExtra("matrix", matrix);
        setResult(RESULT_OK, intent);

        MatrixAddActivity.this.finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

}
