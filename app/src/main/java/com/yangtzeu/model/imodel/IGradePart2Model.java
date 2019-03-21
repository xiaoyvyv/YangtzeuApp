package com.yangtzeu.model.imodel;


import android.app.Activity;

import com.yangtzeu.ui.view.GradePartView2;

public interface IGradePart2Model {
    void loadGradeData(Activity activity, GradePartView2 view);

    void requestGradeData(Activity activity, GradePartView2 view);

    void parseGrade(Activity activity, GradePartView2 view, String data);

    void getGradeXls(Activity activity, GradePartView2 view);
}
