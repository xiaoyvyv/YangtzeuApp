package com.yangtzeu.presenter;

import android.app.Activity;

import com.yangtzeu.model.BoardModel;
import com.yangtzeu.ui.view.BoardView;

public class BoardPresenter {
    private Activity activity;
    private BoardView view;
    private BoardModel mode;

    public BoardPresenter(Activity activity, BoardView view) {
        this.activity = activity;
        this.view = view;
        mode = new BoardModel();
    }

    public void loadBoardData() {
        mode.loadMessageData(activity, view);
    }
}
