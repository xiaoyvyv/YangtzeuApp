package com.yangtzeu.ui.view;

import com.yangtzeu.ui.adapter.GameAdapter;

import androidx.recyclerview.widget.RecyclerView;

public interface GameView {
    RecyclerView getRecyclerView();

    GameAdapter getAdapter();
}
