package com.yangtzeu.ui.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.CleanUtils;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ServiceUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.lib.x5web.X5WebView;
import com.yangtzeu.R;
import com.yangtzeu.entity.Course;
import com.yangtzeu.entity.OnLineBean;
import com.yangtzeu.listener.OnClassListener;
import com.yangtzeu.listener.OnResultListener;
import com.yangtzeu.presenter.MainPresenter;
import com.yangtzeu.receiver.LockReceiver;
import com.yangtzeu.service.BackgroundService;
import com.yangtzeu.ui.activity.base.BaseActivity;
import com.yangtzeu.ui.fragment.GradeFragment;
import com.yangtzeu.ui.fragment.HomeFragment;
import com.yangtzeu.ui.fragment.HomePartFragment3;
import com.yangtzeu.ui.fragment.ManyFragment;
import com.yangtzeu.ui.fragment.MineFragment;
import com.yangtzeu.ui.fragment.TableFragment;
import com.yangtzeu.ui.view.MainView;
import com.yangtzeu.url.Url;
import com.yangtzeu.utils.AlipayUtil;
import com.yangtzeu.utils.MyUtils;
import com.yangtzeu.utils.PollingUtils;
import com.yangtzeu.utils.YangtzeuUtils;

import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, MainView {

    private DrawerLayout drawer;
    private NavigationView leftNavigationView;
    private BottomNavigationView bottomNavigationView;
    private FrameLayout container;
    private ManyFragment manyFragment;
    private HomeFragment homeFragment;
    private TableFragment tableFragment;
    private MineFragment mineFragment;
    private GradeFragment gradeFragment;
    private TextView class_info;
    private LinearLayout have_class;
    private LinearLayout not_have_class;
    private Timer timer1 = new Timer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }


    @Override
    public void findViews() {
        leftNavigationView = findViewById(R.id.nav_view);
        drawer = findViewById(R.id.drawer_layout);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        container = findViewById(R.id.container);

        View headerView = leftNavigationView.getHeaderView(0);
        class_info = headerView.findViewById(R.id.class_info);

        have_class = headerView.findViewById(R.id.have_class);
        not_have_class = headerView.findViewById(R.id.not_have_class);
    }

    @Override
    public void setEvents() {
        timer1 = new Timer();

        leftNavigationView.setNavigationItemSelectedListener(this);
        homeFragment = new HomeFragment();
        homeFragment.setUserVisibleHint(true);
        manyFragment = new ManyFragment();
        gradeFragment = new GradeFragment();
        tableFragment = new TableFragment();
        mineFragment = new MineFragment();

        MainPresenter presenter = new MainPresenter(this, this);
        presenter.setBottomViewWithFragment();
        presenter.initEvents();

        //更新一天课程完成信息
        final Handler classPlanHandler = new Handler(getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                setClassPlan();
            }
        };

        timer1.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Message msg = Message.obtain();
                classPlanHandler.sendMessage(msg);
            }
        }, 0, 5000L);


        boolean is_hide_many_page = SPUtils.getInstance("app_info").getBoolean("is_hide_many_page", false);
        if (is_hide_many_page) {
            bottomNavigationView.inflateMenu(R.menu.main_bottom_menu_hide);
        } else {
            bottomNavigationView.inflateMenu(R.menu.main_bottom_menu);
        }
    }

    private void setClassPlan() {
        YangtzeuUtils.checkHaveClassNow(this, new OnClassListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClass(List<Course> course) {
                int course_size = course.size();
                if (course_size != 0) {
                    have_class.removeAllViews();
                    not_have_class.removeAllViews();
                } else {
                    class_info.setText("今日课程数：" + course_size);
                }
                for (int i = 0; i < course.size(); i++) {
                    Course course_item = course.get(i);
                    String course_title = course_item.getName();
                    course_title = course_title.replace("\"", "");
                    String course_room = course_item.getRoom();
                    course_room = course_room.replace("\"", "");
                    int course_sec = Integer.parseInt(course_item.getSection()) + 1;

                    @SuppressLint("InflateParams")
                    View view = getLayoutInflater().inflate(R.layout.activity_main_header_item, null);
                    TextView title = view.findViewById(R.id.title);
                    TextView time = view.findViewById(R.id.time);
                    TextView room = view.findViewById(R.id.room);

                    title.setText(course_title);
                    time.setText("第" + course_sec + "大节");
                    room.setText(course_room);

                    double time_sec = YangtzeuUtils.getSectionTimeSpan();
                    if (time_sec > course_sec) {
                        have_class.addView(view);
                    } else if (time_sec < course_sec) {
                        not_have_class.addView(view);
                    } else {
                        class_info.setText("正在上课：" + course_title);
                    }
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    //标题栏菜单点击事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.download:
                MyUtils.startActivity(DownloadActivity.class);
                break;
            case R.id.about_me:
                MyUtils.openUrl(MainActivity.this, Url.My_App_Home, true);
                break;
            case R.id.app_trip:
                YangtzeuUtils.getTripInfo(this, true);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private String Theme[] = {"默认", "酷安绿",
            "姨妈红", "哔哩粉", "颐堤蓝",
            "水鸭青", "伊藤橙", "基佬紫",
            "知乎蓝", "古铜棕", "低调灰",
            "高端黒"};
    private int ThemeColor[] = {R.color.colorPrimary, R.color.colorPrimary_KuAnLv, R.color.colorPrimary_YiMaHong,
            R.color.colorPrimary_BiLiFen, R.color.colorPrimary_YiDiLan, R.color.colorPrimary_ShuiYaQing,
            R.color.colorPrimary_YiTengCheng, R.color.colorPrimary_JiLaoZi, R.color.colorPrimary_ZhiHuLan,
            R.color.colorPrimary_GuTongZong, R.color.colorPrimary_DiDiaoHui, R.color.colorPrimary_GaoDuanHei};

    //侧滑抽屉菜单点击事件
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.setting_center:
                MyUtils.startActivity(SettingActivity.class);
                break;
            case R.id.change_theme:
                @SuppressLint("InflateParams")
                View view = getLayoutInflater().inflate(R.layout.view_change_theme, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setPositiveButton(R.string.done, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AppUtils.relaunchApp();
                    }
                });
                builder.setNegativeButton(R.string.clear, null);
                builder.setView(view).show();
                LinearLayout layout = view.findViewById(R.id.container);
                for (int i = 0; i < ThemeColor.length; i++) {
                    @SuppressLint("InflateParams")
                    View m_item = getLayoutInflater().inflate(R.layout.view_choose_term_item, null);
                    TextView title = m_item.findViewById(R.id.title);
                    TextView bt = m_item.findViewById(R.id.bt);
                    title.setText(Theme[i]);
                    title.setTextColor(getResources().getColor(ThemeColor[i]));
                    final int finalI = i;
                    bt.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            SPUtils.getInstance("app_info").put("theme", String.valueOf(finalI));
                            ToastUtils.showShort("你选择了：" + Theme[finalI]);
                        }
                    });
                    layout.addView(m_item);
                }
                break;
            case R.id.term_year:
                YangtzeuUtils.showChooseTerm(this, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SPUtils.getInstance("user_info").put("term_id", String.valueOf(which));
                    }
                });
                break;

            case R.id.clear:
                FileUtils.deleteAllInDir(MyUtils.rootPath() + "A_Tool/Cache/");
                ToastUtils.showShort(R.string.clean_cache_success);
                break;
            case R.id.manger:
                String number = SPUtils.getInstance("user_info").getString("number");
                if (StringUtils.equals(number, "201603246") || StringUtils.equals(number, "201602810")) {
                    ToastUtils.showShort("欢迎您");
                    MyUtils.startActivity(MangerActivity.class);
                } else {
                    ToastUtils.showShort("抱歉，您不是管理员！");
                }
                break;
            case R.id.collection:
                MyUtils.startActivity(CollectionActivity.class);
                break;
            case R.id.about:
                MyUtils.startActivity(AboutActivity.class);
                break;
            case R.id.check_update:
                ToastUtils.showShort(R.string.check_version);
                YangtzeuUtils.checkAppVersion(this);
                break;
            case R.id.exit:
                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setTitle(R.string.trip)
                        .setMessage(R.string.is_exit)
                        .setPositiveButton(R.string.done, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityUtils.finishAllActivities();
                                AppUtils.exitApp();
                            }
                        })
                        .setNegativeButton(R.string.clear, null)
                        .create();
                dialog.show();
                break;
            case R.id.red_bag:
                if (AlipayUtil.hasInstalledAlipayClient(MainActivity.this)) {
                    MyUtils.putStringToClipboard(MainActivity.this, getString(R.string.apply_redbag_key));
                    MyUtils.showRedBag(MainActivity.this);
                } else {
                    ToastUtils.showLong(R.string.no_apply_app);
                }
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onBackPressed() {
        TabLayout.Tab item0 = Objects.requireNonNull(HomeFragment.tabLayout.getTabAt(0));
        TabLayout.Tab item2 = Objects.requireNonNull(HomeFragment.tabLayout.getTabAt(2));
        X5WebView web = HomePartFragment3.webView;
        MenuItem view1 = bottomNavigationView.getMenu().getItem(0);

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (!view1.isChecked()) {
            bottomNavigationView.setSelectedItemId(view1.getItemId());
        } else if (web != null && web.canGoBack()) {
            web.goBack();
            if (!item2.isSelected()) {
                item2.select();
            }
        } else if (item0 != null && !item0.isSelected()) {
            item0.select();
        } else {
            onClick();
        }

    }


    private boolean isExit = false;

    private void onClick() {
        if (!isExit) {
            isExit = true;
            ToastUtils.showShort(getString(R.string.double_exit));
            new Handler(getMainLooper()) {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    isExit = false;
                }
            }.sendEmptyMessageDelayed(0, 2000); // 利用handler延迟发送更改状态信息
        } else {
            ActivityUtils.finishAllActivities();
            AppUtils.exitApp();
            android.os.Process.killProcess(android.os.Process.myPid());//再此之前可以做些退出等操作
        }
    }

    @Override
    public FrameLayout getFragmentContainer() {
        return container;
    }


    @Override
    public HomeFragment getHomeFragment() {
        return homeFragment;
    }

    @Override
    public GradeFragment getGradeFragment() {
        return gradeFragment;
    }

    @Override
    public TableFragment getTableFragment() {
        return tableFragment;
    }

    @Override
    public ManyFragment getManyFragment() {
        return manyFragment;
    }

    @Override
    public MineFragment getMineFragment() {
        return mineFragment;
    }

    @Override
    public DrawerLayout getDrawerLayout() {
        return drawer;
    }


    @Override
    public BottomNavigationView getBottomNavigationView() {
        return bottomNavigationView;
    }

    @Override
    protected void onDestroy() {
        BackgroundService.stop(this);
        if (timer1 != null) {
            timer1.cancel();
        }
        super.onDestroy();
    }

}
