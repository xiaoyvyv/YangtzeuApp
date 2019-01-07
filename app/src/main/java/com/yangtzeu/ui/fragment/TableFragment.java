package com.yangtzeu.ui.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.android.material.tabs.TabLayout;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yangtzeu.R;
import com.yangtzeu.entity.Course;
import com.yangtzeu.presenter.TablePresenter;
import com.yangtzeu.ui.activity.base.BaseFragment;
import com.yangtzeu.ui.adapter.TableFragmentAdapter;
import com.yangtzeu.ui.view.TableView;
import com.yangtzeu.url.Url;
import com.yangtzeu.utils.MyUtils;
import com.yangtzeu.utils.UriPathUtils;
import com.yangtzeu.utils.YangtzeuUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Administrator on 2018/3/6.
 */

public class TableFragment extends BaseFragment implements TableView {
    // 标志位，标志已经初始化完成。
    private boolean isPrepared = false;
    private Toolbar toolbar;
    private View rootView;
    private TabLayout tabLayout;
    private LinearLayout week_layout;
    private RecyclerView recyclerView;
    private TablePresenter presenter;
    private SmartRefreshLayout refreshLayout;
    private TableFragmentAdapter adapter;
    private List<Course> courses = new ArrayList<>();
    private ImageView table_bg;
    private FragmentActivity activity;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_table, container, false);
        findViews();
        isPrepared = true;
        lazyLoad();
        return rootView;
    }

    @Override
    public void findViews() {
        recyclerView = rootView.findViewById(R.id.recyclerView);
        toolbar = rootView.findViewById(R.id.toolbar);
        week_layout = rootView.findViewById(R.id.week_layout);
        refreshLayout = rootView.findViewById(R.id.refreshLayout);
        tabLayout = rootView.findViewById(R.id.tabLayout);
        table_bg = rootView.findViewById(R.id.table_bg);

    }

    @Override
    public void setEvents() {
        activity = getActivity();
        adapter = new TableFragmentAdapter(getActivity());
        presenter = new TablePresenter(getActivity(), this);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(adapter);

        presenter.setWithTabLayout();
        presenter.setTableBackground();


        toolbar.inflateMenu(R.menu.table_menu);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.choose_date:
                        YangtzeuUtils.showChooseTerm(Objects.requireNonNull(getActivity()), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SPUtils.getInstance("user_info").put("term_id", String.valueOf(which));
                                SPUtils.getInstance("user_info").put("table_week", 1);

                                tabLayout.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Objects.requireNonNull(tabLayout.getTabAt(0)).select();
                                    }
                                }, 100);
                            }
                        });
                        break;
                    case R.id.web_table:
                        MyUtils.openUrl(activity, Url.Yangtzeu_Table_Ids);
                        break;
                    case R.id.table_bg_white:
                        SPUtils.getInstance("app_info").put("table_bg", Url.Yangtzeu_Table_Background_White);
                        MyUtils.loadImage(activity, table_bg, Url.Yangtzeu_Table_Background_White);
                        break;
                    case R.id.default_bg:
                        SPUtils.getInstance("app_info").put("table_bg", Url.Yangtzeu_Table_Background);
                        MyUtils.loadImage(activity, table_bg, Url.Yangtzeu_Table_Background);
                        break;
                    case R.id.table_bg:
                        Intent pickIntent = new Intent(Intent.ACTION_PICK);
                        pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                        startActivityForResult(pickIntent, 200);
                        break;
                    case R.id.alpha:
                        @SuppressLint("InflateParams")
                        View view = LayoutInflater.from(activity).inflate(R.layout.view_change_alpha, null);
                        final SeekBar bar = view.findViewById(R.id.seekBar);
                        final CardView sample = view.findViewById(R.id.sample);
                        String alpha = SPUtils.getInstance("app_info").getString("table_alpha", "ff");
                        bar.setProgress(Integer.parseInt(alpha, 16));

                        sample.setCardBackgroundColor(Color.parseColor("#"+alpha+"76b1e9"));
                        bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                            @Override
                            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                                sample.setCardBackgroundColor(Color.parseColor("#"+MyUtils.formattingH(progress)+"76b1e9"));
                            }
                            @Override
                            public void onStartTrackingTouch(SeekBar seekBar) { }
                            @Override
                            public void onStopTrackingTouch(SeekBar seekBar) { }
                        });

                        AlertDialog alert = MyUtils.getAlert(getActivity(), null, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SPUtils.getInstance("app_info").put("table_alpha", MyUtils.formattingH(bar.getProgress()));
                                refreshLayout.autoRefresh();
                            }
                        });
                        alert.setTitle(null);
                        alert.setView(view);
                        alert.show();
                        break;
                }
                return false;
            }
        });

        refreshLayout.setEnableLoadMore(false);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                courses.clear();
                adapter.clear();
                presenter.loadTableData();
            }
        });


    }

    @Override
    public Toolbar getToolbar() {
        return toolbar;
    }

    @Override
    public LinearLayout getWeekLayout() {
        return week_layout;
    }

    @Override
    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    @Override
    public List<Course> getCourse() {
        return courses;
    }

    @Override
    public SmartRefreshLayout getRefreshLayout() {
        return refreshLayout;
    }

    @Override
    public TabLayout getTabLayout() {
        return tabLayout;
    }

    @Override
    public TableFragmentAdapter getTableFragmentAdapter() {
        return adapter;
    }

    @Override
    public ImageView getTableBackgroundView() {
        return table_bg;
    }


    @Override
    protected void lazyLoad() {
        if (!isPrepared || !isVisible) {
            return;
        }
        if (!isLoadFinish) {
            setEvents();
            isLoadFinish = true;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == 200) {
            String bg_path = MyUtils.rootPath() + "A_Tool/table_background.jpg";
            File new_file = new File(bg_path);
            FileUtils.copyFile(new File(Objects.requireNonNull(UriPathUtils.getPath(getActivity(), data.getData()))), new_file);
            SPUtils.getInstance("app_info").put("table_bg", bg_path);
            LogUtils.i("课表图片：" + bg_path);
            table_bg.setScaleType(ImageView.ScaleType.CENTER_CROP);
            MyUtils.loadImageNoCache(getActivity(), table_bg, bg_path);
        } else if (resultCode == Activity.RESULT_CANCELED) {
            ToastUtils.showShort(R.string.clear_set);
        } else ToastUtils.showShort(R.string.load_file_error);
    }
}
