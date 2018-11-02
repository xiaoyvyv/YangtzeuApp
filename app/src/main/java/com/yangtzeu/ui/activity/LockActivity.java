package com.yangtzeu.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.blankj.utilcode.util.CacheDiskUtils;
import com.skyfishjy.library.RippleBackground;
import com.yangtzeu.R;
import com.yangtzeu.presenter.LockPresenter;
import com.yangtzeu.ui.activity.base.BaseActivity;
import com.yangtzeu.ui.view.LockView;
import com.yangtzeu.utils.DeviceUtils;
import com.yangtzeu.utils.MyUtils;

import androidx.appcompat.widget.Toolbar;


public class LockActivity extends BaseActivity implements LockView {
    private Toolbar toolbar;
    private DeviceUtils deviceUtils;
    private LockPresenter presenter;
    private long lock_mill = 0;
    private Button start;
    private RippleBackground rippleBackground;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock);
        init();
        MyUtils.setToolbarBackToHome(this, toolbar);
    }

    @Override
    public void findViews() {
        toolbar = findViewById(R.id.toolbar);
        start = findViewById(R.id.start);
        rippleBackground = findViewById(R.id.content);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(R.string.clean_permission).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                deviceUtils.onRemoveActivate();
                return false;
            }
        }).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void setEvents() {
        //进程不同，用文件存储数据，以便时实更新
        lock_mill = Long.parseLong(CacheDiskUtils.getInstance(MyUtils.geCacheDir()).getString("lock_time", "0"));

        rippleBackground.startRippleAnimation();
        deviceUtils = new DeviceUtils(LockActivity.this);
        presenter = new LockPresenter(this, this);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (deviceUtils.isActivate()) {
                    presenter.setLockTime();
                } else {
                    AlertDialog dialog = MyUtils.getAlert(LockActivity.this, "提示：请你务必看完以下内容！\n\n1.此功能必须使用设备管理器权限！\n2.激活权限后才可使用！\n\n注意：若你需要卸载本应用，请先关闭此项权限！", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            deviceUtils.onActivate();
                        }
                    });
                    dialog.show();
                    dialog.setCanceledOnTouchOutside(false);
                }

            }
        });

        //检查是上次否锁定完成
        presenter.checkIsLockFinish(this, null, "ACTION_SCREEN_CHECK");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public long getLockTimeMill() {
        return lock_mill;
    }

    @Override
    public void setLockTimeMill(long time) {
        this.lock_mill = time;
    }
}
