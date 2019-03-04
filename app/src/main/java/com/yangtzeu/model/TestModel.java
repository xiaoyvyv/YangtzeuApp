package com.yangtzeu.model;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.FrameLayout;

import com.blankj.utilcode.util.FragmentUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.android.material.tabs.TabLayout;
import com.yangtzeu.R;
import com.yangtzeu.http.OkHttp;
import com.yangtzeu.http.OnResultStringListener;
import com.yangtzeu.model.imodel.ITestModel;
import com.yangtzeu.ui.fragment.TestFragment;
import com.yangtzeu.ui.view.TestView;
import com.yangtzeu.url.Url;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import okhttp3.FormBody;
import okhttp3.Request;

public class TestModel implements ITestModel {

    @Override
    public void getTestInfo(final Activity activity, final TestView view) {
        view.getTabLayout().removeAllTabs();
        view.getContainer().removeAllViews();
        view.getFragments().clear();

        view.getProgressDialog().show();
        String nowId = SPUtils.getInstance("user_info").getString("test_id",Url.Default_Term);

        FormBody formBody = new FormBody.Builder()
                .add("semester.id", nowId)
                .build();

        Request request = new Request.Builder()
                .url(view.getUrl())
                .post(formBody)
                .build();

        OkHttp.do_Post(request, new OnResultStringListener() {
            @Override
            public void onResponse(String response) {
                LogUtils.e(response);

                view.getProgressDialog().dismiss();
                Document document = Jsoup.parse(response);
                Elements options = document.select("#examBatchId>option");
                if (response.contains("我的考试")) {
                    parseTestInfo(activity, view, options);
                } else {
                    ToastUtils.showShort(R.string.load_error);
                }
            }

            @Override
            public void onFailure(String error) {
                view.getProgressDialog().dismiss();
                ToastUtils.showShort(R.string.try_again);
            }
        });
    }

    @Override
    public void parseTestInfo(final Activity activity, final TestView view, Elements options) {
        int tabSize = options.size();
        if (tabSize == 0) {
            String nowId = SPUtils.getInstance("user_info").getString("test_id",Url.Default_Term);
            AlertDialog.Builder build = new AlertDialog.Builder(activity);
            build.setTitle(R.string.trip);
            build.setMessage("当前学期（ID："+nowId+"）未设置排考批次\n*确定>>>查询上一学期考试安排\n*取消>>>右上角选择查询学期");
            build.setPositiveButton(R.string.done, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String nowId = SPUtils.getInstance("user_info").getString("test_id", Url.Default_Term);
                    int term = Integer.parseInt(nowId) - 1;
                    if (term==68) term = 49;
                    if (term==47) term = 46;
                    SPUtils.getInstance("user_info").put("test_id", String.valueOf(term));
                    getTestInfo(activity, view);
                }
            });
            build.setNegativeButton(R.string.clear, null);
            build.create();
            AlertDialog dialog = build.create();
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
            return;
        }

        FrameLayout container = view.getContainer();
        FragmentManager manager = view.getManager();
        FragmentUtils.removeAll(manager);

        final List<Fragment> fragments = view.getFragments();
        container.removeAllViews();

        for (int i = 0; i < tabSize; i++) {
            String title = options.get(i).text();
            String exam_batch = options.get(i).attr("value");

            TabLayout.Tab tab = view.getTabLayout().newTab();
            tab.setText(title);
            view.getTabLayout().addTab(tab);

            TestFragment testFragment = TestFragment.newInstance(title, exam_batch);
            fragments.add(testFragment);

            if (i == 0) {
                FragmentUtils.add(manager, testFragment, view.getContainer().getId(), false);
                testFragment.setUserVisibleHint(true);
            } else {
                FragmentUtils.add(manager, testFragment, view.getContainer().getId(), true);
            }
        }


        view.getTabLayout().addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (fragments.size() != 0) {
                    TestFragment testFragment = (TestFragment) fragments.get(tab.getPosition());
                    testFragment.setUserVisibleHint(true);
                    FragmentUtils.showHide(testFragment, fragments);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}
