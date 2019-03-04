package com.yangtzeu.presenter;

import android.app.Activity;

import com.yangtzeu.model.CetModel;
import com.yangtzeu.model.GameModel;
import com.yangtzeu.ui.view.CetView;
import com.yangtzeu.ui.view.GameView;

public class GamePresenter {
    private Activity activity;
    private GameView view;
    private GameModel mode;

    public GamePresenter(Activity activity, GameView view) {
        this.activity = activity;
        this.view = view;
        mode = new GameModel();
    }

    public void loadGames() {
        mode.loadGames(activity, view);
    }
}
