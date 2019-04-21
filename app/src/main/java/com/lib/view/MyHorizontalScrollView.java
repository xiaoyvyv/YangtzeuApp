package com.lib.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.HorizontalScrollView;

import java.util.Timer;
import java.util.TimerTask;

public class MyHorizontalScrollView extends HorizontalScrollView {
    public boolean isEnd = false;
    private Timer timer;
    private long period = 2000;


    public MyHorizontalScrollView(Context context) {
        super(context);
        initThis();
    }

    public MyHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initThis();
    }

    public MyHorizontalScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initThis();
    }

    public MyHorizontalScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initThis();
    }

    private void initThis() {
        timer = new Timer();
        start();
    }


    public void start() {
        timer.schedule(new MyTimeTask(), 0, period);
    }

    public void setPeriod(long period) {
        this.period = period;
        if (timer != null) {
            timer.cancel();
            timer.purge();
            timer = null;
        }
        initThis();
    }

    public class MyTimeTask extends TimerTask {
        @Override
        public void run() {
            if (isEnd) {
                smoothScrollTo(0, 0);
                isEnd = false;
            } else {
                smoothScrollTo(MyHorizontalScrollView.this.getWidth(), 0);
                isEnd = true;
            }
        }
    }
}
