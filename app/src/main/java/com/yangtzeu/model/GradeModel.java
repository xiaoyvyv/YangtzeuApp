package com.yangtzeu.model;

import android.app.Activity;

import com.yangtzeu.R;
import com.yangtzeu.model.imodel.IGradeModel;
import com.yangtzeu.ui.fragment.GradePartFragment1;
import com.yangtzeu.ui.fragment.GradePartFragment2;
import com.yangtzeu.ui.view.GradeView;
import com.yangtzeu.url.Url;

public class GradeModel implements IGradeModel {

    @Override
    public void setViewPager(Activity activity, GradeView view) {
        GradePartFragment1 fragment1 = GradePartFragment1.newInstance(Url.Yangtzeu_Grade_Url);
        GradePartFragment1 fragment2 = GradePartFragment1.newInstance(Url.Yangtzeu_AllGrade_Url + "&semesterId=");
        GradePartFragment2 fragment3 = new GradePartFragment2();


        view.getAdapter().addFragment(activity.getString(R.string.term_grade), fragment1);
        view.getAdapter().addFragment(activity.getString(R.string.all_grade), fragment2);
        view.getAdapter().addFragment(activity.getString(R.string.grade_point), fragment3);

        view.getViewPager().setAdapter(view.getAdapter());
        view.getViewPager().setOffscreenPageLimit(2);

        view.getTabLayout().setupWithViewPager(view.getViewPager());
    }


}
