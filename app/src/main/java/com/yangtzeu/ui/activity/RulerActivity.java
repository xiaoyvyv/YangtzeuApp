package com.yangtzeu.ui.activity;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;

import com.blankj.utilcode.util.ScreenUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.lib.ruler.circle.CycleRulerView;
import com.lib.ruler.line.RulerView;
import com.yangtzeu.R;
import com.yangtzeu.ui.activity.base.BaseActivity;

public class RulerActivity extends BaseActivity {
    private RulerView rulerView;
    private CycleRulerView cycleRulerView;
    private FloatingActionButton change;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ScreenUtils.setFullScreen(this);
        super.onCreate(savedInstanceState);
        ScreenUtils.setLandscape(this);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        setContentView(R.layout.activity_ruler);
        init();
    }

    @Override
    public void findViews() {
        change = findViewById(R.id.change);
        rulerView = findViewById(R.id.rulerView);
        cycleRulerView = findViewById(R.id.cycleRulerView);
    }

    @Override
    public void setEvents() {
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int visibility = rulerView.getVisibility();
                if (visibility == View.GONE) {
                    rulerView.setVisibility(View.VISIBLE);
                    cycleRulerView.setVisibility(View.GONE);
                } else {
                    rulerView.setVisibility(View.GONE);
                    cycleRulerView.setVisibility(View.VISIBLE);
                }
            }
        });

    }
}
