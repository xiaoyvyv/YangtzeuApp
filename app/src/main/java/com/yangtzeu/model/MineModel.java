package com.yangtzeu.model;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.provider.Settings;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.lib.chat.common.UserManager;
import com.lib.chat.listener.handler.MessageHandler;
import com.xiaomi.mimc.common.MIMCConstant;
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

import androidx.cardview.widget.CardView;

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

        String qq = SPUtils.getInstance("user_info").getString("qq", "default_header");
        if (!qq.equals("default_header")) {
            String qqHeader = MyUtils.getQQHeader(qq);
            MyUtils.loadImage(activity, view.getHeader(), qqHeader);
        } else {
            MyUtils.loadImage(activity, view.getHeader(), qq);
        }

        YangtzeuUtils.keepOnline(new OnResultListener<OnLineBean>() {
            @Override
            public void onResult(OnLineBean s) {
                int size = s.getSize();
                SPUtils.getInstance("app_info").put("online_size", size);
                Intent intent = new Intent();
                intent.setAction("Online_BroadcastReceiver");
                intent.putExtra("online_size", size);
                activity.sendBroadcast(intent);
            }
        });

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

        //刷新即时消息的未读数目
        MessageHandler.sendReceiveMessageBroadcast();
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
    public void checkInternetView(final Activity activity, final MineView view) {
        final TextView internet_state = view.getInternetState();
        final LinearLayout physicalLayout = view.getPhysicalLayout();
        final LinearLayout cardLayout = view.getCardLayout();
        if (NetworkUtils.isWifiConnected()) {
            internet_state.setTextColor(activity.getResources().getColor(R.color.colorPrimary));
            internet_state.setText("校园网检测中");

            OkHttp.do_Get("http://10.151.0.249/", new OnResultStringListener() {
                @Override
                public void onResponse(String response) {
                    if (response.contains("已使用时间")) {
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
                    } else {
                        internet_state.setTextColor(Color.parseColor("#aaff0000"));
                        internet_state.setText("未连接到校园网");
                    }
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

    @Override
    public void loadDayTrip(final Activity activity, final MineView view) {
        //ImageView dayTripImage = view.getDayTripImage();
        final TextView statusTextView = view.getStatusTextView();
        final CardView statusView = view.getStatusView();

        MIMCConstant.OnlineStatus status = UserManager.getInstance().getStatus();
        if (status == MIMCConstant.OnlineStatus.ONLINE) {
            statusTextView.setText(activity.getString(R.string.online));
            statusView.setCardBackgroundColor(Color.GREEN);
        } else {
            statusTextView.setText(activity.getString(R.string.outline));
            statusView.setCardBackgroundColor(Color.RED);
        }

        statusView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showShort("聊天系统状态：" + statusTextView.getText());
            }
        });
    }
}
