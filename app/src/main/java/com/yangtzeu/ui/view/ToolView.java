package com.yangtzeu.ui.view;


import com.yangtzeu.ui.adapter.ToolAdapter;

import androidx.recyclerview.widget.RecyclerView;

public interface ToolView {
    RecyclerView getRecyclerView();

    ToolAdapter getAdapter();
}
