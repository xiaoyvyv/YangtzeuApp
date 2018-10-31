package com.yangtzeu.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.RemoteViews;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.yangtzeu.R;
import com.yangtzeu.ui.activity.MainActivity;
import com.yangtzeu.ui.activity.SplashActivity;
import com.yangtzeu.url.Url;
import com.yangtzeu.utils.MyUtils;

import java.io.File;
import java.util.Objects;

import androidx.annotation.NonNull;

/**
 * Created by Administrator on 2018/2/26.
 */

public class ClassWidgetProvider extends AppWidgetProvider {
    public final static String TEMP_CLICK = "WidgetProvider_TEMP_CLICK";
    public final static String CHANGE_CLICK = "WidgetProvider_CHANGE_CLICK";
    public final static String REFRESH_CLICK = "WidgetProvider_REFRESH_CLICK";
    public final static String GRID_VIEW_ITEM_CLICK = "WidgetProvider_GRIDVIEW_ITEM_CLICK";
    public final static String GRID_VIEW_ITEM_EXTRA = "WidgetProvider_GRIDVIEW_ITEM_EXTRA";

    /**
     * 每次窗口小部件被更新都调用一次该方法
     */
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        refreshWidget(context, appWidgetManager, appWidgetIds);
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    //刷新控件
    private void refreshWidget(final Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            final RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.view_widget_layout);

            String week = String.valueOf(MyUtils.getStudyWeek());
            remoteViews.setTextViewText(R.id.now, "当前周次：" + week);

            Intent open = new Intent(context.getApplicationContext(), SplashActivity.class);
            open.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            //设置课表标题点击事件
            PendingIntent temp_pending = getPendingIntent(context, getMyIntent(context, TEMP_CLICK), R.id.temp);
            setOnClickListener(remoteViews, R.id.temp, temp_pending);


            //设置课表周次改变点击事件
            PendingIntent change_pending = getPendingIntent(context, getMyIntent(context, CHANGE_CLICK), R.id.change);
            setOnClickListener(remoteViews, R.id.change, change_pending);

            //设置课表刷新点击事件
            PendingIntent refresh_pending = getPendingIntent(context, getMyIntent(context, REFRESH_CLICK), R.id.refresh);
            setOnClickListener(remoteViews, R.id.refresh, refresh_pending);


            //设置GridView的adapter
            Intent serviceIntent = new Intent(context, ClassWidgetProviderService.class);
            remoteViews.setRemoteAdapter(R.id.gridView, serviceIntent);


            // 设置GridView的子布局响应的intent模板
            Intent gridIntent = getMyIntent(context, GRID_VIEW_ITEM_CLICK);
            gridIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            PendingIntent pendingIntent = getPendingIntent(context, gridIntent, 0);
            remoteViews.setPendingIntentTemplate(R.id.gridView, pendingIntent);


            // 调用集合管理器对集合进行更新
            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);

        }
    }

    //获取控件PendingIntent
    private PendingIntent getPendingIntent(Context context, Intent intent, int item_id) {
        return PendingIntent.getBroadcast(context, item_id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private Intent getMyIntent(Context context, String action) {
        Intent intent = new Intent(context, ClassWidgetProvider.class);
        intent.setAction(action);
        return intent;
    }

    //设置控件点击事件
    private void setOnClickListener(RemoteViews remoteViews, int item_id, PendingIntent pendingIntent) {
        remoteViews.setOnClickPendingIntent(item_id, pendingIntent);
    }


    /**
     * 接收窗口小部件点击时发送的广播
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        switch (Objects.requireNonNull(action)) {
            case TEMP_CLICK:
                Intent open = new Intent(context.getApplicationContext(), MainActivity.class);
                open.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(open);
                break;
            case CHANGE_CLICK:
                Intent appIntent = new Intent(context.getApplicationContext(), SplashActivity.class);
                appIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(appIntent);
                break;
            case REFRESH_CLICK:
                ComponentName cn = new ComponentName(context, ClassWidgetProvider.class);
                appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetManager.getAppWidgetIds(cn), R.id.gridView);

                // 更新 Widget
                RemoteViews remoteViews = new RemoteViews(context.getApplicationContext().getPackageName(), R.layout.view_widget_layout);
                appWidgetManager.updateAppWidget(cn, remoteViews);

                ToastUtils.showLong(R.string.widget_trip);
                break;
            case GRID_VIEW_ITEM_CLICK:
                // 接受gridView的点击事件的广播
                String section = intent.getStringExtra("section");
                String week = intent.getStringExtra("week");
                String room = intent.getStringExtra("room");
                String name = intent.getStringExtra("name");
                ToastUtils.showLong(name + "\n星期" + (Integer.parseInt(week) + 1) + "--第" + (Integer.parseInt(section) + 1) + "大节\n" + room);
                break;
        }
        super.onReceive(context, intent);
    }


    /**
     * 每删除一次窗口小部件就调用一次
     */
    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
    }

    /**
     * 当最后一个该窗口小部件删除时调用该方法
     */
    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
    }

    /**
     * 当该窗口小部件第一次添加到桌面时调用该方法
     */
    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
    }


    /**
     * 当小部件大小改变时
     */
    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
    }

    /**
     * 当小部件从备份恢复时调用该方法
     */
    @Override
    public void onRestored(Context context, int[] oldWidgetIds, int[] newWidgetIds) {
        super.onRestored(context, oldWidgetIds, newWidgetIds);
    }

}
