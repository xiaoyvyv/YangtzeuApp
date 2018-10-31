package com.yangtzeu.presenter;

import android.app.Activity;

import com.yangtzeu.model.TableModel;
import com.yangtzeu.ui.view.TableView;

public class TablePresenter {
    private TableModel model;
    private TableView view;
    private Activity activity;

    public TablePresenter(Activity activity, TableView view) {
        this.view = view;
        this.activity = activity;
        model = new TableModel();
    }

    public void setWithTabLayout() {
        model.setWithTabLayout(activity, view);
    }

    public void loadTableData() {
        model.loadTableDataStep1(activity, view);
    }

    public void setTableBackground() {
        model.setTableBackground(activity, view);
    }
}
