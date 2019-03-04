package com.yangtzeu.model;
import android.app.Activity;
import android.widget.FrameLayout;

import com.blankj.utilcode.util.FragmentUtils;
import com.google.android.material.tabs.TabLayout;
import com.yangtzeu.model.imodel.IPlanModel;
import com.yangtzeu.ui.activity.PlanActivity;
import com.yangtzeu.ui.fragment.PlanPartFragment1;
import com.yangtzeu.ui.fragment.PlanPartFragment2;
import com.yangtzeu.ui.view.PlanView;

import androidx.fragment.app.FragmentManager;

public class PlanModel implements IPlanModel {
    @Override
    public void getPlanInfo(Activity activity, PlanView view) {
        FrameLayout container = view.getContainer();
        container.removeAllViews();
        FragmentManager fm = ((PlanActivity) activity).getSupportFragmentManager();
        FragmentUtils.removeAll(fm);

        final PlanPartFragment1 part1 = new PlanPartFragment1();
        final PlanPartFragment2 part2 = new PlanPartFragment2();

        FragmentUtils.add(fm, part1, container.getId(), false);
        part1.setUserVisibleHint(true);

        FragmentUtils.add(fm, part2, container.getId(), true);

        TabLayout tabLayout = view.getTabLayout();
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        FragmentUtils.showHide(part1, part2);
                        part1.setUserVisibleHint(true);
                        if (PlanPartFragment1.presenter!=null)
                            PlanPartFragment1.presenter.loadPlan();
                        break;
                    case 1:
                        FragmentUtils.showHide(part2, part1);
                        part2.setUserVisibleHint(true);
                        if (PlanPartFragment2.presenter!=null)
                            PlanPartFragment2.presenter.showPlanList();
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        FragmentUtils.showHide(part1, part2);
                        part1.setUserVisibleHint(true);
                        if (PlanPartFragment1.presenter!=null)
                            PlanPartFragment1.presenter.loadPlan();
                        break;
                    case 1:
                        FragmentUtils.showHide(part2, part1);
                        part2.setUserVisibleHint(true);
                        if (PlanPartFragment2.presenter!=null)
                            PlanPartFragment2.presenter.showPlanList();
                        break;
                }
            }
        });
    }

}
