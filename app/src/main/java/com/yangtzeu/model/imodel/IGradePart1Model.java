package com.yangtzeu.model.imodel;


import android.app.Activity;

import com.yangtzeu.ui.view.GradePartView1;
import com.yangtzeu.ui.view.HomePartView1;

public interface IGradePart1Model {
    void loadGradeData(Activity activity, GradePartView1 view);

    void requestGradeData(Activity activity, GradePartView1 view);

    void parseGrade(Activity activity, GradePartView1 view, String data);

    void getGradeXls(Activity activity, GradePartView1 view);
}
