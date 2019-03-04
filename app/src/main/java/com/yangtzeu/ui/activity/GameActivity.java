package com.yangtzeu.ui.activity;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;

import com.yangtzeu.R;
import com.yangtzeu.presenter.GamePresenter;
import com.yangtzeu.ui.activity.base.BaseActivity;
import com.yangtzeu.ui.adapter.GameAdapter;
import com.yangtzeu.ui.view.GameView;
import com.yangtzeu.utils.MyUtils;

public class GameActivity extends BaseActivity implements GameView {

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private GameAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        init();
        MyUtils.setToolbarBackToHome(this, toolbar);
    }

    @Override
    public void findViews() {
        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recyclerView);

    }

    @Override
    public void setEvents() {
        GamePresenter presenter = new GamePresenter(this, this);
        adapter = new GameAdapter(this);
        recyclerView.setAdapter(adapter);

        presenter.loadGames();
    }

    @Override
    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    @Override
    public GameAdapter getAdapter() {
        return adapter;
    }
}
