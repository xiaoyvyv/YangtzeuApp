package com.yangtzeu.ui.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.SwitchPreference;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.yangtzeu.R;
import com.yangtzeu.utils.AlipayUtil;
import com.yangtzeu.utils.AppIconUtils;
import com.yangtzeu.utils.MyUtils;
import com.yangtzeu.utils.UserUtils;
import com.yangtzeu.utils.YangtzeuUtils;

import androidx.appcompat.widget.Toolbar;


public class SettingActivity extends PreferenceActivity {
    private final static String TAG = "【SettingActivity】";
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MyUtils.setMyTheme(SettingActivity.this);
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.setting_preference);
        toolbar.setTitle(getTitle());
        SetUp();
    }

    private void SetUp() {

        //文件保存路径设置
        final EditTextPreference FileLocation = (EditTextPreference) findPreference("FileLocation");
        FileLocation.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                String input = o.toString();
                if (!(input.substring(input.length() - 1, input.length())).equals("/")) {
                    input = input + "/";
                } else if ((input.substring(0, 1)).equals("/")) {
                    input = input.substring(1, input.length());
                }
                Log.e(TAG, "保存的文件下载路径" + input);
                SPUtils.getInstance("app_info").put("save_path", input);

                MyUtils.createSDCardDir(input);
                FileLocation.setSummary(input);
                return true;
            }
        });

        //修改密码
        Preference ChangePassword = findPreference("ChangePassword");
        ChangePassword.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                MyUtils.startActivity(ChangePassActivity.class);
                return true;
            }
        });

        String name = SPUtils.getInstance("user_info").getString("name", "未知");
        //切换用户
        Preference Login = findPreference("do_login");
        Login.setSummary("用户：" + name);
        Login.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                UserUtils.do_Logout(SettingActivity.this);
                return true;
            }
        });

        Preference mShare = findPreference("share");
        mShare.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                MyUtils.shareText(SettingActivity.this, "推荐了Yangtzeu.app\n\n一个超级好用的长大教务处\n下载地址：https://www.coolapk.com/apk/com.yangtzeu");
                return true;
            }
        });

        final Preference Update = findPreference("Update");
        String UpDateInfo = SPUtils.getInstance("app_info").getString("update", "暂无更新！");
        Update.setSummary(UpDateInfo);

        Update.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                ToastUtils.showShort(R.string.check_version);
                YangtzeuUtils.checkAppVersion(SettingActivity.this);
                return true;
            }
        });
        Preference OpenCode = findPreference("OpenCode");
        OpenCode.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                @SuppressLint("InflateParams")
                AlertDialog dialog = new AlertDialog.Builder(SettingActivity.this)
                        .setView(getLayoutInflater().inflate(R.layout.view_open_dialog, null))
                        .setPositiveButton(R.string.done, null)
                        .create();
                dialog.show();
                return true;
            }
        });

        boolean is_hide_many_page = SPUtils.getInstance("app_info").getBoolean("is_hide_many_page", false);
        SwitchPreference hide_many = (SwitchPreference) findPreference("hide_many");
        hide_many.setChecked(!is_hide_many_page);
        hide_many.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                boolean newValue1 = !(boolean) newValue;
                SPUtils.getInstance("app_info").put("is_hide_many_page", newValue1);
                if (newValue1) {
                    ToastUtils.showShort(R.string.hide_success);
                } else {
                    ToastUtils.showShort(R.string.show_success);
                }
                return true;
            }
        });


        Preference icon = findPreference("icon");
        icon.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public boolean onPreferenceClick(Preference preference) {
                int[] icons = {R.mipmap.ic_launcher, R.mipmap.ic_launcher1, R.mipmap.ic_launcher2};
                @SuppressLint("InflateParams")
                View view = getLayoutInflater().inflate(R.layout.view_choose_icon, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
                builder.setView(view);
                final AlertDialog dialog = builder.create();
                dialog.show();
                LinearLayout layout = view.findViewById(R.id.container);
                for (int i = 0; i < icons.length; i++) {
                    @SuppressLint("InflateParams")
                    View item = getLayoutInflater().inflate(R.layout.view_choose_icon_item, null);
                    TextView title = item.findViewById(R.id.title);
                    ImageView icon = item.findViewById(R.id.icon);
                    Glide.with(SettingActivity.this).load(icons[i]).into(icon);
                    TextView bt = item.findViewById(R.id.bt);
                    title.setText("图标" + i);
                    final int finalI = i;
                    bt.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            ToastUtils.showLong("更换桌面图标成功，等待桌面刷新后即可");
                            SPUtils.getInstance("app_info").put("app_icon", "app_icon_" + finalI);
                            new AppIconUtils().pmTest(SettingActivity.this);
                        }
                    });
                    layout.addView(item);
                }

                return true;
            }
        });

        Preference pay = findPreference("red");
        pay.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                MyUtils.putStringToClipboard(SettingActivity.this, getString(R.string.apply_redbag_key));
                if (AlipayUtil.hasInstalledAlipayClient(SettingActivity.this)) {
                    MyUtils.openUrl(SettingActivity.this, getString(R.string.red_url));
                } else {
                    ToastUtils.showLong(R.string.no_apply_app);
                }
                return true;
            }
        });

        Preference donate = findPreference("donate");
        donate.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                MyUtils.putStringToClipboard(SettingActivity.this, getString(R.string.apply_redbag_key));
                if (AlipayUtil.hasInstalledAlipayClient(SettingActivity.this)) {
                    //第二个参数代表要给被支付的二维码code  可以在用草料二维码在线生成
                    AlipayUtil.startAlipayClient(SettingActivity.this, getString(R.string.apply_me_key));
                } else {
                    ToastUtils.showLong(R.string.no_apply_app);
                }
                return true;
            }
        });
        final Preference myApp = findPreference("myApp");
        myApp.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                MyUtils.startActivity(AppListActivity.class);
                return true;
            }
        });


    }


    @Override
    public void setContentView(int layoutResID) {
        ViewGroup contentView = (ViewGroup) LayoutInflater
                .from(this)
                .inflate(R.layout.activity_settings, new LinearLayout(this), false);

        toolbar = contentView.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingActivity.this.finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        contentView.findViewById(R.id.exit)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ActivityUtils.finishAllActivities();
                        AppUtils.exitApp();
                    }
                });

        ViewGroup contentWrapper = contentView.findViewById(R.id.content_wrapper);

        LayoutInflater
                .from(this)
                .inflate(layoutResID, contentWrapper, true);
        getWindow().setContentView(contentView);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        SettingActivity.this.finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
}