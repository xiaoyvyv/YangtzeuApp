package com.yangtzeu.listener;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.yangtzeu.ui.activity.JwcListActivity;
import com.yangtzeu.url.Url;
import com.yangtzeu.utils.MyUtils;

public class HomePart1Listener {

    public static class OnListener1 implements View.OnClickListener {
        private final Context context;

        public OnListener1(Context context) {
            this.context = context;
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(context, JwcListActivity.class);
            intent.putExtra("title", "教务通知");
            intent.putExtra("from_url", Url.Yangtzeu_JWTZ);
            MyUtils.startActivity(intent);
        }
    }

    public static class OnListener2 implements View.OnClickListener {
        private final Context context;

        public OnListener2(Context context) {
            this.context = context;
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(context, JwcListActivity.class);
            intent.putExtra("title", "本周事务");
            intent.putExtra("from_url", Url.Yangtzeu_BZSW);
            MyUtils.startActivity(intent);
        }
    }

    public static class OnListener3 implements View.OnClickListener {
        private final Context context;

        public OnListener3(Context context) {
            this.context = context;
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(context, JwcListActivity.class);
            intent.putExtra("title", "教学动态");
            intent.putExtra("from_url", Url.Yangtzeu_JXDT);
            MyUtils.startActivity(intent);
        }
    }

    public static class OnListener4 implements View.OnClickListener {
        private final Context context;

        public OnListener4(Context context) {
            this.context = context;
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(context, JwcListActivity.class);
            intent.putExtra("title", "教学简报");
            intent.putExtra("from_url", Url.Yangtzeu_JXJB);
            MyUtils.startActivity(intent);
        }
    }
}
