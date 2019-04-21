package com.yangtzeu.ui.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.lib.chat.common.UserManager;
import com.xiaomi.mimc.common.MIMCConstant;
import com.yangtzeu.R;
import com.yangtzeu.listener.OnResultListener;
import com.yangtzeu.presenter.MinePresenter;
import com.yangtzeu.ui.activity.ADActivity;
import com.yangtzeu.ui.activity.AnswerWebActivity;
import com.yangtzeu.ui.activity.BoardActivity;
import com.yangtzeu.ui.activity.CetActivity;
import com.yangtzeu.ui.activity.ChangePassActivity;
import com.yangtzeu.ui.activity.ChatActivity;
import com.yangtzeu.ui.activity.ChooseClassActivity;
import com.yangtzeu.ui.activity.FeedBackActivity;
import com.yangtzeu.ui.activity.InfoActivity;
import com.yangtzeu.ui.activity.PingJiaoActivity;
import com.yangtzeu.ui.activity.PlanActivity;
import com.yangtzeu.ui.activity.TestActivity;
import com.yangtzeu.ui.activity.base.BaseFragment;
import com.yangtzeu.ui.view.MineView;
import com.yangtzeu.url.Url;
import com.yangtzeu.utils.EmptyTableUtils;
import com.yangtzeu.utils.GoogleUtils;
import com.yangtzeu.utils.MyUtils;
import com.yangtzeu.utils.UserUtils;

import java.util.Objects;

/**
 * Created by Administrator on 2018/3/6.
 */

@SuppressLint("SetTextI18n")
public class MineFragment extends BaseFragment implements MineView {
    // 标志位，标志已经初始化完成。
    private boolean isPrepared = false;
    private View rootView;
    private TextView userName;
    private TextView mClass;
    private ImageView userHeader;
    private ImageView image_bg;
    private Toolbar toolbar;
    private MinePresenter presenter;
    private SwipeRefreshLayout refreshLayout;

    private TextView online;
    private TextView unReadView;
    private TextView message;
    private TextView messageImage;
    private LinearLayout messageLayout;
    private LinearLayout empty_room;
    private LinearLayout changeLayout;
    private LinearLayout school_plan;
    private LinearLayout chooseClass;
    private LinearLayout testLayout;
    private LinearLayout physicalLayout;
    private LinearLayout cardLayout;
    private TextView internet_state;
    private LinearLayout boardLayout;
    private LinearLayout feedBackLayout;
    private LinearLayout cetLayout;
    private LinearLayout pingJiaoLayout;
    private LinearLayout logoutLayout;
    private LinearLayout coolLayout;
    private LinearLayout githubLayout;
    private LinearLayout feeLayout;
    private LinearLayout planLayout;
    private LinearLayout adLayout;


    private Activity activity;
    private OnlineBroadcastReceiver onlineBroadcastReceiver;
    private MIMCOnlineBroadcastReceiver mimcOnlineBroadcastReceiver;
    private ReceiveMessageBroadcastReceiver receiveMessageBroadcastReceiver;

    private boolean isReceiver = false;
    private TextView status_trip;
    private TextView exit_text;
    private CardView status;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_mine, container, false);
        findViews();
        isPrepared = true;
        lazyLoad();
        return rootView;
    }

    @Override
    public void findViews() {
        toolbar = rootView.findViewById(R.id.toolbar);
        userName = rootView.findViewById(R.id.userName);
        mClass = rootView.findViewById(R.id.mClass);
        userHeader = rootView.findViewById(R.id.userHeader);
        refreshLayout = rootView.findViewById(R.id.refreshLayout);
        image_bg = rootView.findViewById(R.id.image_bg);
        status_trip = rootView.findViewById(R.id.status_trip);
        status = rootView.findViewById(R.id.status);
        exit_text = rootView.findViewById(R.id.exit_text);


        unReadView = rootView.findViewById(R.id.unReadView);
        online = rootView.findViewById(R.id.online);
        boardLayout = rootView.findViewById(R.id.boardLayout);
        message = rootView.findViewById(R.id.message);
        internet_state = rootView.findViewById(R.id.internet_state);
        messageLayout = rootView.findViewById(R.id.messageLayout);
        messageImage = rootView.findViewById(R.id.messageImage);
        changeLayout = rootView.findViewById(R.id.changeLayout);
        empty_room = rootView.findViewById(R.id.empty_room);
        school_plan = rootView.findViewById(R.id.school_plan);
        chooseClass = rootView.findViewById(R.id.chooseClass);
        testLayout = rootView.findViewById(R.id.testLayout);
        physicalLayout = rootView.findViewById(R.id.physicalLayout);
        cardLayout = rootView.findViewById(R.id.cardLayout);
        feedBackLayout = rootView.findViewById(R.id.feedBackLayout);
        cetLayout = rootView.findViewById(R.id.cetLayout);
        pingJiaoLayout = rootView.findViewById(R.id.pingJiaoLayout);
        logoutLayout = rootView.findViewById(R.id.logoutLayout);
        githubLayout = rootView.findViewById(R.id.githubLayout);
        coolLayout = rootView.findViewById(R.id.coolLayout);
        feeLayout = rootView.findViewById(R.id.feeLayout);
        planLayout = rootView.findViewById(R.id.planLayout);
        adLayout = rootView.findViewById(R.id.adLayout);


    }

    @Override
    public void setEvents() {
        activity = getActivity();

        int size = SPUtils.getInstance("app_info").getInt("online_size", 0);
        online.setText("online：" + size);

        //在线人数统计
        IntentFilter filter = new IntentFilter();
        filter.addAction("Online_BroadcastReceiver");
        onlineBroadcastReceiver = new OnlineBroadcastReceiver();
        activity.registerReceiver(onlineBroadcastReceiver, filter);

        //小米及时云消息在线状态广播接收器
        IntentFilter filter1 = new IntentFilter();
        filter1.addAction("MIMC_Online_BroadcastReceiver");
        mimcOnlineBroadcastReceiver = new MIMCOnlineBroadcastReceiver();
        activity.registerReceiver(mimcOnlineBroadcastReceiver, filter1);

        //小米及时云接收消息来了的广播接收器
        IntentFilter filter2 = new IntentFilter();
        filter2.addAction("ReceiveMessage_BroadcastReceiver");
        receiveMessageBroadcastReceiver = new ReceiveMessageBroadcastReceiver();
        activity.registerReceiver(receiveMessageBroadcastReceiver, filter2);


        isReceiver = true;

        presenter = new MinePresenter(getActivity(), this);
        presenter.loadDayTrip();
        presenter.loadUserInfo();
        presenter.loadMessage();
        presenter.checkInternetView();
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.loadDayTrip();
                presenter.loadUserInfo();
                presenter.loadMessage();
                presenter.checkInternetView();
            }
        });


        online.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyUtils.openUrl(activity, Url.Yangtzeu_App_Online_Show, true);
            }
        });

        userHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), InfoActivity.class);
                intent.putExtra("id", UserManager.getInstance().getAccount());
                intent.putExtra("name", SPUtils.getInstance("user_info").getString("name"));
                MyUtils.startActivity(intent);
            }
        });

        internet_state.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.checkInternetView();
            }
        });


        image_bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ObjectUtils.isEmpty(UserManager.getInstance().getStatus())) {
                    ToastUtils.showShort("当前处于离线状态，请稍后再试");
                    return;
                }
                if (ObjectUtils.equals(UserManager.getInstance().getStatus(),MIMCConstant.OnlineStatus.OFFLINE )) {
                    ToastUtils.showShort("当前处于离线状态，请稍后再试");
                    return;
                }
                MyUtils.startActivity(ChatActivity.class);
            }
        });


        empty_room.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EmptyTableUtils.startGetForm(getActivity());
            }
        });

        school_plan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyUtils.openUrl(Objects.requireNonNull(getActivity()), Url.Yangtzeu_School_Plan, true);
            }
        });

        chooseClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyUtils.startActivity(ChooseClassActivity.class);
            }
        });

        testLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyUtils.startActivity(TestActivity.class);
            }
        });


        boardLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyUtils.startActivity(BoardActivity.class);
            }
        });

        physicalLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (internet_state.getText().toString().equals("未连接到校园网")) {
                    presenter.showSchoolDialog();
                } else
                    ToastUtils.showShort("请等待网络检查完毕后再操作");
            }
        });

        cardLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (internet_state.getText().toString().equals("未连接到校园网")) {
                    presenter.showSchoolDialog();
                } else
                    ToastUtils.showShort("请等待网络检查完毕后再操作");
            }
        });

        changeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyUtils.startActivity(ChangePassActivity.class);
            }
        });

        feedBackLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyUtils.startActivity(FeedBackActivity.class);
            }
        });

        cetLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyUtils.startActivity(CetActivity.class);
            }
        });

        pingJiaoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyUtils.startActivity(PingJiaoActivity.class);
            }
        });

        coolLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyUtils.launchMarketApp(AppUtils.getAppPackageName(), "com.coolapk.market");
            }
        });

        adLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyUtils.startActivity(ADActivity.class);
            }
        });


        GoogleUtils.loadInterstitialAd();
        githubLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder build = new AlertDialog.Builder(getActivity());
                build.setTitle(R.string.trip);
                build.setMessage("源码已经上传到Github，如果您也想学习此软件源码，请加群讨论！");
                build.setPositiveButton(R.string.done, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MyUtils.openUrl(getActivity(), Url.Yangtzeu_Join_Group,true);
                    }
                });
                build.setNeutralButton("ReadMe.MD", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        GoogleUtils.showInterstitialAd(new OnResultListener<Boolean>() {
                            @Override
                            public void onResult(Boolean s) {
                                Intent intent = new Intent(getActivity(), AnswerWebActivity.class);
                                intent.putExtra("from_url", Url.Yangtzeu_Github);
                                MyUtils.startActivity(intent);
                            }
                        });
                    }
                });
                build.setNegativeButton(R.string.clear, null);
                build.create();
                AlertDialog dialog = build.create();
                dialog.show();
                dialog.setCanceledOnTouchOutside(false);
            }
        });

        feeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyUtils.openUrl(Objects.requireNonNull(getActivity()), Url.Yangtzeu_Fee, true);
            }
        });

        planLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyUtils.startActivity(PlanActivity.class);
            }
        });


        boolean is_offline_mode = SPUtils.getInstance("user_info").getBoolean("offline_mode", false);
        if (is_offline_mode) {
            exit_text.setTextColor(Color.RED);
            exit_text.setText("退出离线模式");
            logoutLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog dialog = new AlertDialog.Builder(getActivity())
                            .setTitle(R.string.trip)
                            .setMessage(R.string.is_logout_offmode)
                            .setPositiveButton(R.string.done, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    UserUtils.do_Logout(getActivity());
                                    SPUtils.getInstance("user_info").put("offline_mode", false);
                                }
                            })
                            .setNegativeButton(R.string.clear, null)
                            .create();
                    dialog.show();
                }
            });
        } else {
            exit_text.setText("退出登录");
            logoutLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog dialog = new AlertDialog.Builder(getActivity())
                            .setTitle(R.string.trip)
                            .setMessage(R.string.is_logout)
                            .setPositiveButton(R.string.done, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    UserUtils.do_Logout(getActivity());
                                }
                            })
                            .setNegativeButton(R.string.clear, null)
                            .create();
                    dialog.show();
                }
            });
        }
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
    public ImageView getHeader() {
        return userHeader;
    }

    @Override
    public TextView getClassView() {
        return mClass;
    }

    @Override
    public TextView getNameView() {
        return userName;
    }

    @Override
    public Toolbar getToolbar() {
        return toolbar;
    }


    @Override
    public TextView getMessageView() {
        return message;
    }

    @Override
    public CardView getStatusView() {
        return status;
    }

    @Override
    public TextView getStatusTextView() {
        return status_trip;
    }

    @Override
    public LinearLayout getMessageLayout() {
        return messageLayout;
    }

    @Override
    public LinearLayout getPhysicalLayout() {
        return physicalLayout;
    }

    @Override
    public LinearLayout getCardLayout() {
        return cardLayout;
    }

    @Override
    public TextView getMessageImage() {
        return messageImage;
    }

    @Override
    public SwipeRefreshLayout getRefresh() {
        return refreshLayout;
    }

    @Override
    public TextView getInternetState() {
        return internet_state;
    }

    @Override
    public void onDestroy() {
        if (onlineBroadcastReceiver != null && isReceiver) {
            activity.unregisterReceiver(onlineBroadcastReceiver);
        }

        if (mimcOnlineBroadcastReceiver != null && isReceiver) {
            activity.unregisterReceiver(mimcOnlineBroadcastReceiver);
        }

        if (receiveMessageBroadcastReceiver != null && isReceiver) {
            activity.unregisterReceiver(receiveMessageBroadcastReceiver);
        }
        super.onDestroy();
    }

    //在线人数广播
    public class OnlineBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            int size = intent.getIntExtra("online_size", 0);
            online.setText("online：" + size);
        }
    }

    //MIMC状态广播
    public class MIMCOnlineBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean is_mimc_online = intent.getBooleanExtra("online_status", false);
            if (is_mimc_online) {
                status_trip.setText(activity.getString(R.string.online));
                status.setCardBackgroundColor(Color.GREEN);
            } else {
                status_trip.setText(activity.getString(R.string.outline));
                status.setCardBackgroundColor(Color.RED);
            }
        }
    }

    //接收到即时消息
    public class ReceiveMessageBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            long unRead = UserManager.getInstance().getAllUnRead();
            if (unRead != 0) {
                unReadView.setText(unRead + "条未读聊天消息");
                unReadView.setTextColor(Color.RED);
            } else {
                unReadView.setText("点击进入聊天系统");
                unReadView.setTextColor(Color.WHITE);
            }
        }
    }
}
