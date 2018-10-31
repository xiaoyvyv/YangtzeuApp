package com.yangtzeu.model.imodel;


import android.app.Activity;

import com.yangtzeu.ui.view.TableView;

public interface ITableModel {

    void setWithTabLayout(Activity activity, TableView view);

    void loadTableDataStep1(Activity activity, TableView view);

    void loadTableDataStep2(Activity activity, TableView view);

    void setTableBackground(Activity activity, TableView view);
}
