package com.yangtzeu.model;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.provider.Settings;
import android.text.InputType;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.yangtzeu.R;
import com.yangtzeu.entity.MessageBean;
import com.yangtzeu.entity.OnLineBean;
import com.yangtzeu.http.OkHttp;
import com.yangtzeu.http.OnResultStringListener;
import com.yangtzeu.listener.OnResultListener;
import com.yangtzeu.model.imodel.IMineModel;
import com.yangtzeu.ui.activity.CardCenterActivity;
import com.yangtzeu.ui.activity.MessageActivity;
import com.yangtzeu.ui.activity.PhysicalActivity;
import com.yangtzeu.ui.view.MineView;
import com.yangtzeu.url.Url;
import com.yangtzeu.utils.MyUtils;
import com.yangtzeu.utils.YangtzeuUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.appcompat.widget.Toolbar;

public class MineModel implements IMineModel {

    @SuppressLint("SetTextI18n")
    @Override
    public void loadUserInfo(final Activity activity, MineView view) {
        //姓名
        String userName = SPUtils.getInstance("user_info").getString("name");
        view.getNameView().setText(userName);
        //班级
        String userClass = SPUtils.getInstance("user_info").getString("class");
        view.getClassView().setText(userClass);
        String qq = SPUtils.getInstance("user_info").getString("qq", "2440888027");
        String qqHeader = MyUtils.getQQHeader(qq);

        Glide.with(activity).load(qqHeader).into(view.getHeader());

        YangtzeuUtils.keepOnline(new OnResultListener<OnLineBean>() {
            @Override
            public void onResult(OnLineBean s) {
                int size = s.getSize();
                SPUtils.getInstance("app_info").put("online_size", size);
                Intent intent = new Intent();
                intent.setAction("Online_BroadcastReceiver");
                intent.putExtra("online_size",size);
                activity.sendBroadcast(intent);
            }
        });

    }


    @Override
    public void showChangeHeaderView(final Activity activity, final MineView mineView) {

        LayoutInflater inflater = LayoutInflater.from(activity);
        @SuppressLint("InflateParams")
        View MeDialog = inflater.inflate(R.layout.view_change_header, null);

        final AlertDialog dialog = new AlertDialog.Builder(activity, R.style.style_dialog)
                .setView(MeDialog)
                .create();
        dialog.show();

        TextView QQ_Head = MeDialog.findViewById(R.id.QQ_Head);
        QQ_Head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                dialog.dismiss();

                @SuppressLint("InflateParams") final View view = activity.getLayoutInflater().inflate(R.layout.view_input_qq, null);
                final TextInputEditText inputEditText = view.findViewById(R.id.text);
                TextView textView = view.findViewById(R.id.trip);
                textView.setText(R.string.input_qq);

                String qq = SPUtils.getInstance("user_info").getString("qq");
                inputEditText.setTextSize(15);
                inputEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
                inputEditText.setText(qq);
                inputEditText.setSelectAllOnFocus(true);
                inputEditText.setFocusable(true);

                AlertDialog dialog = new AlertDialog.Builder(activity)
                        .setView(view)
                        .setNegativeButton(R.string.clear, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                MyUtils.canCloseDialog(dialogInterface, true);
                            }
                        })
                        .setNeutralButton(R.string.clean, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //自动弹出键盘
                                KeyboardUtils.showSoftInput(inputEditText);
                                inputEditText.setText(null);
                                MyUtils.canCloseDialog(dialogInterface, false);
                            }
                        })
                        .setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String qq = Objects.requireNonNull(inputEditText.getText()).toString().trim();
                                if (!qq.isEmpty()) {

                                    KeyboardUtils.hideSoftInput(activity);
                                    SPUtils.getInstance("user_info").put("qq", qq);
                                    MyUtils.canCloseDialog(dialogInterface, true);

                                    Glide.with(activity).load(MyUtils.getQQHeader(qq)).into(mineView.getHeader());
                                } else {
                                    MyUtils.canCloseDialog(dialogInterface, false);
                                    ToastUtils.showShort(R.string.input_qq);
                                }
                            }
                        }).create();
                dialog.show();

            }
        });
        TextView Clear = MeDialog.findViewById(R.id.clear);
        Clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        Window window = dialog.getWindow();
        Objects.requireNonNull(window).setGravity(Gravity.BOTTOM);  //此处可以设置dialog显示的位置
        window.getDecorView().setPadding(0, 0, 0, 0);
        window.setWindowAnimations(R.style.BottomToBottom);  //添加动画
        WindowManager windowManager = activity.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = display.getWidth(); //设置宽度
        dialog.getWindow().setAttributes(lp);

    }

    private int not_message_size = 0;

    @Override
    public void loadMessage(final Activity activity, final MineView view) {
        not_message_size = 0;
        final String number = SPUtils.getInstance("user_info").getString("number");

        String url = Url.getMessage(number);
        OkHttp.do_Get(url, new OnResultStringListener() {
            @Override
            public void onResponse(String response) {
                view.getRefresh().setRefreshing(false);
                final MessageBean bean = (new Gson()).fromJson(response, MessageBean.class);
                String info = bean.getInfo();
                if (info.contains("查询成功")) {
                    if (ObjectUtils.isNotEmpty(bean.getData())) {
                        final List<MessageBean.DataBean> data = bean.getData();

                        final List<String> ids = new ArrayList<>();

                        for (int i = 0; i < data.size(); i++) {
                            String to = data.get(i).getTo();
                            String id = data.get(i).getId();
                            String read = data.get(i).getRead();
                            //只过滤当前用户的消息
                            if (!read.equals("true")) {
                                not_message_size++;
                                ids.add(id);
                            } else {
                                if (to.equals("all_yangtzeu")) {
                                    String message_info = SPUtils.getInstance("all_message_info").getString(id);
                                    if (ObjectUtils.isEmpty(message_info)) {
                                        not_message_size++;
                                        SPUtils.getInstance("all_message_info").put(id, id);
                                    }
                                }
                            }
                        }

                        if (not_message_size != 0) {
                            view.getMessageImage().setText(String.valueOf(not_message_size));
                            view.getMessageView().setText(R.string.new_message);
                            view.getMessageImage().setVisibility(View.VISIBLE);
                            view.getMessageLayout().setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    view.getMessageView().setText(R.string.message);
                                    setMessageRead(ids);
                                    view.getMessageImage().setVisibility(View.GONE);

                                    MyUtils.startActivity(MessageActivity.class);
                                }
                            });
                        } else {
                            view.getMessageLayout().setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    MyUtils.startActivity(MessageActivity.class);
                                    ToastUtils.showShort(R.string.no_new_message);
                                }
                            });
                        }
                    } else {
                        LogUtils.e("信息暂无数据");
                    }
                } else {
                    ToastUtils.showShort(info);
                }
            }

            @Override
            public void onFailure(String error) {
                view.getRefresh().setRefreshing(false);
                ToastUtils.showShort(R.string.load_error);
            }
        });
    }

    @Override
    public void setMessageRead(List<String> ids) {
        for (String id : ids) {
            OkHttp.do_Get(Url.getReadMessage(id), new OnResultStringListener() {
                @Override
                public void onResponse(String response) {
                    LogUtils.i(response);
                }

                @Override
                public void onFailure(String error) {
                    LogUtils.e(error);
                }
            });

        }
    }

    @Override
    public void setToolbarEvent(final Activity activity, MineView view) {
        view.getToolbar().inflateMenu(R.menu.mine_menu);
        view.getToolbar().setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.me_info:
                        MyUtils.openUrl(activity, Url.Yangtzeu_XueJI);
                        break;
                }
                return false;
            }
        });

    }

    @Override
    public void checkInternetView(final Activity activity, final MineView view) {
        final TextView internet_state = view.getInternetState();
        final LinearLayout physicalLayout = view.getPhysicalLayout();
        final LinearLayout cardLayout = view.getCardLayout();
        if (NetworkUtils.isWifiConnected()) {
            internet_state.setTextColor(activity.getResources().getColor(R.color.colorPrimary));
            internet_state.setText("校园网检测中");

            OkHttp.do_Get("http://10.10.240.250", new OnResultStringListener() {
                @Override
                public void onResponse(String response) {
                    LogUtils.e(response);
                    internet_state.setTextColor(Color.parseColor("#aa00ff00"));
                    internet_state.setText("已连接到校园网");


                    physicalLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (NetworkUtils.isWifiAvailable()) {
                                MyUtils.startActivity(PhysicalActivity.class);
                            } else showSchoolDialog(activity);
                        }
                    });

                    cardLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (NetworkUtils.isWifiAvailable()) {
                                MyUtils.startActivity(CardCenterActivity.class);
                            } else showSchoolDialog(activity);
                        }
                    });
                }

                @Override
                public void onFailure(String error) {
                    internet_state.setTextColor(Color.parseColor("#aaff0000"));
                    internet_state.setText("未连接到校园网");
                }
            });
        } else {
            internet_state.setTextColor(Color.parseColor("#aaff0000"));
            internet_state.setText("未连接到校园网");
        }
    }

    @Override
    public void showSchoolDialog(Activity activity) {
        AlertDialog dialog = new AlertDialog.Builder(activity)
                .setTitle("提示")
                .setMessage("此模块必须使用校园网，请您将手机处于校园网内后重试")
                .setNegativeButton("取消", null)
                .setPositiveButton("连接校园WiFi", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MyUtils.startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }
                })
                .create();
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
    }
}
