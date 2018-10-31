package com.yangtzeu.widget;

import android.annotation.SuppressLint;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.SPUtils;
import com.yangtzeu.R;
import com.yangtzeu.database.DatabaseUtils;
import com.yangtzeu.entity.Course;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class ClassWidgetProviderService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new GridRemoteViewsFactory(this.getApplicationContext(), intent);
    }

    private class GridRemoteViewsFactory implements RemoteViewsFactory {
        private Context mContext;
        int mAppWidgetId;
        @SuppressLint("UseSparseArrays")
        private HashMap<Integer, Course[]> booleans = new HashMap<>();
        private int count = 42;

        GridRemoteViewsFactory(Context context, Intent intent) {
            mContext = context;
            mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }


        @Override
        public void onCreate() {
            // 初始化集合中的数据
            for (int i = 0; i < getCount(); i++) {
                Course courses[] = new Course[2];
                booleans.put(i, courses);
            }

            List<Course> courses = DatabaseUtils.getHelper(mContext, "table.db").queryAll(Course.class);
            if (ObjectUtils.isNotEmpty(courses)) {
                for (int i = 0; i < courses.size(); i++) addCourse(courses.get(i));
            }
        }

        private void addCourse(Course course) {
            int week = 0;
            int section = 0;
            Course courses[] = new Course[2];
            if (!course.getWeek().isEmpty()) {
                week = Integer.parseInt(course.getWeek());
            }
            if (!course.getSection().isEmpty()) {
                section = Integer.parseInt(course.getSection());
            }
            int where = GetPositionByWhichAndSection(week + 1, section + 1);
            if (Objects.requireNonNull(booleans.get(where))[0] == null) {
                courses[0] = course;
                booleans.put(where, courses);
            } else {
                LogUtils.e();
                courses[0] = Objects.requireNonNull(booleans.get(where))[0];
                courses[1] = course;
                booleans.put(where, courses);
            }
        }

        public void clear() {
            for (int i = 0; i < getCount(); i++) {
                Course courses[] = new Course[2];
                booleans.put(i, courses);
            }
        }

        private int GetPositionByWhichAndSection(int Week, int Section) {
            return (Section * 7 - 1) - (7 - Week);
        }

        @Override
        public RemoteViews getViewAt(int position) {
            RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.view_widget_layout_item);
            Course[] courses = booleans.get(position);

            assert courses != null;
            Course course =courses[0];
            final Course course_more = courses[1];

            if (course == null) {
                views.setTextViewText(R.id.text, "");
                return views;
            }

            String name = course.getName();
            name = name.replace("\"", "");
            String room = course.getRoom();
            room = room.replace("\"", "");
            final String teacher = course.getTeacher();

            views.setTextViewText(R.id.text, name + "\n\n" + teacher + "\n\n" + room);

            if (course_more != null) {
                String name_more = course_more.getName();
                name_more = name_more.replace("\"", "");
                final String finalName_more = name_more;
                views.setTextViewText(R.id.text, name + "\n\n" + finalName_more + "\n\n" + room);
            }


            final String section = course.getSection();
            final String week = course.getWeek();

            Intent fillInIntent = new Intent();
            fillInIntent.putExtra(ClassWidgetProvider.GRID_VIEW_ITEM_EXTRA, position);
            fillInIntent.putExtra("name", name);
            fillInIntent.putExtra("section", section);
            fillInIntent.putExtra("week", week);
            fillInIntent.putExtra("room", room);
            views.setOnClickFillInIntent(R.id.text, fillInIntent);
            return views;
        }

        @Override
        public int getCount() {
            return count;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public void onDataSetChanged() {
            LogUtils.e("数据改变");
        }

        @Override
        public void onDestroy() {
            clear();
        }

    }
}
