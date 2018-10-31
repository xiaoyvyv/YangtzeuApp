package com.yangtzeu.ui.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yangtzeu.R;
import com.yangtzeu.presenter.MinePresenter;
import com.yangtzeu.ui.activity.BoardActivity;
import com.yangtzeu.ui.activity.CetActivity;
import com.yangtzeu.ui.activity.ChangePassActivity;
import com.yangtzeu.ui.activity.ChooseClassActivity;
import com.yangtzeu.ui.activity.FeedBackActivity;
import com.yangtzeu.ui.activity.MoreActivity;
import com.yangtzeu.ui.activity.ShopActivity;
import com.yangtzeu.ui.activity.TestActivity;
import com.yangtzeu.ui.activity.base.BaseFragment;
import com.yangtzeu.ui.view.MineView;
import com.yangtzeu.url.Url;
import com.yangtzeu.utils.EmptyTableUtils;
import com.yangtzeu.utils.MyUtils;
import com.yangtzeu.utils.UserUtils;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

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
    private SmartRefreshLayout refreshLayout;

    private TextView online;
    private TextView userCetView;
    private TextView userTest;
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
    private Activity activity;
    private OnlineBroadcastReceiver onlineBroadcastReceiver;
    private boolean isReceiver = false;

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


        online = rootView.findViewById(R.id.online);
        boardLayout = rootView.findViewById(R.id.boardLayout);
        userCetView = rootView.findViewById(R.id.user_cet);
        userTest = rootView.findViewById(R.id.user_test);
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


    }

    @Override
    public void setEvents() {
        activity = getActivity();

        int size = SPUtils.getInstance("app_info").getInt("online_size", 0);
        online.setText("online：" + size);

        IntentFilter filter = new IntentFilter();
        filter.addAction("Online_BroadcastReceiver");
        onlineBroadcastReceiver = new OnlineBroadcastReceiver();
        activity.registerReceiver(onlineBroadcastReceiver, filter);
        isReceiver = true;

        refreshLayout.setEnableLoadMore(false);
        presenter = new MinePresenter(getActivity(), this);
        presenter.setToolbarEvent();

        refreshLayout.setEnableLoadMore(false);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                presenter.loadUserInfo();
                presenter.loadMessage();
                presenter.checkInternetView();
            }
        });
        refreshLayout.autoRefresh();



        userHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.showChangeHeaderView();
            }
        });

        internet_state.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.checkInternetView();
            }
        });


        
        image_bg.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                MyUtils.startActivity(ShopActivity.class);
                return true;
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
                MyUtils.openUrl(Objects.requireNonNull(getActivity()), Url.Yangtzeu_School_Plan);
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
                MyUtils.openUrl(Objects.requireNonNull(getActivity()), Url.Yangtzeu_Teacher);
            }
        });

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
    public TextView getUserCetView() {
        return userCetView;
    }

    @Override
    public TextView getEmailView() {
        return userTest;
    }

    @Override
    public TextView getMessageView() {
        return message;
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
    public SmartRefreshLayout getRefresh() {
        return refreshLayout;
    }

    @Override
    public TextView getInternetState() {
        return internet_state;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (onlineBroadcastReceiver != null && isReceiver) {
            activity.unregisterReceiver(onlineBroadcastReceiver);
        }
    }

    public class OnlineBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            int size = intent.getIntExtra("online_size", 0);
            online.setText("online：" + size);
        }
    }
}
