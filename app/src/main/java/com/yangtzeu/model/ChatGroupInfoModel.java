package com.yangtzeu.model;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.Utils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.textfield.TextInputEditText;
import com.lib.chat.adapter.GroupInnerUserAdapter;
import com.lib.chat.bean.ChatInfoBean;
import com.lib.chat.bean.ContactGroupBean;
import com.lib.chat.bean.CreateGroupBean;
import com.lib.chat.bean.GroupExtraBean;
import com.lib.chat.common.Constant;
import com.lib.chat.common.UserManager;
import com.lib.subutil.GsonUtils;
import com.yangtzeu.R;
import com.yangtzeu.http.OnResultStringListener;
import com.yangtzeu.model.imodel.IChatGroupInfoModel;
import com.yangtzeu.ui.activity.ChatActivity;
import com.yangtzeu.ui.fragment.ChatFragment3;
import com.yangtzeu.ui.view.ChatGroupInfoView;
import com.yangtzeu.ui.view.ChatView;
import com.yangtzeu.utils.MyUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import cn.bingoogolapple.bgabanner.BGABanner;

public class ChatGroupInfoModel implements IChatGroupInfoModel {

    private UserManager instance;
    public ChatGroupInfoModel () {
        instance = UserManager.getInstance();
    }

    @Override
    public void loadGroupInfo(final Activity activity, final ChatGroupInfoView view) {
        final LinearLayout rootView = view.getMineInfoLayout();
        final TextView ownerView = rootView.findViewById(R.id.ownerAccount);
        final TextView bulletinView = rootView.findViewById(R.id.bulletin);

        instance.queryGroupInfo(view.getTopicId(), new OnResultStringListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(String response) {
                CreateGroupBean createGroupBean = GsonUtils.fromJson(response, CreateGroupBean.class);
                int code = createGroupBean.getCode();
                if (code == 200) {
                    CreateGroupBean.DataBean data = createGroupBean.getData();
                    final String topicId = data.getTopicInfo().getTopicId();
                    view.getNumber().setText(topicId);

                    String topicName = data.getTopicInfo().getTopicName();
                    view.getName().setText(topicName);

                    Object bulletin = data.getTopicInfo().getBulletin();
                    String ownerAccount = data.getTopicInfo().getOwnerAccount();
                    loadToolbarEvent(activity, view, data);

                    ownerView.setText("群主：" + ownerAccount);
                    if (ObjectUtils.isEmpty(bulletin) || ObjectUtils.equals(bulletin, null)) {
                        bulletinView.setText("公告：当前群未发布公告！");
                    } else {
                        bulletinView.setText("公告：" + String.valueOf(bulletin));
                    }

                    List<CreateGroupBean.DataBean.MembersBean> members = data.getMembers();
                    //加载群成员
                    loadMembersInfo(activity, view, data);

                    //解析扩展信息
                    String extra = String.valueOf(data.getTopicInfo().getExtra());
                    loadExtraInfo(activity, view, extra);
                } else {
                    ToastUtils.showShort(createGroupBean.getMessage());
                }
            }

            @Override
            public void onFailure(String error) {
                ToastUtils.showShort("操作：失败！");
            }
        });
    }

    private void loadToolbarEvent(final Activity activity, final ChatGroupInfoView view, final CreateGroupBean.DataBean topicInfo) {
        Toolbar toolbar = view.getToolbar();
        Menu menu = toolbar.getMenu();
        menu.removeGroup(0);

        //若为群主
        if (StringUtils.equals(UserManager.getInstance().getAccount(), topicInfo.getTopicInfo().getOwnerAccount())) {
            menu.add(0, 50, 0, "发布公告")
                    .setIcon(R.drawable.ic_send)
                    .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            menu.add(0, 100, 0, "解散该群");
        } else {
            menu.add(0, 200, 0, "退出该群");
        }
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case 50:
                        updateGroupInfo(activity, view, topicInfo, true);
                        break;
                    case 100:
                        if (StringUtils.equals(view.getTopicId(), Constant.YANGTZEU_GROUP1)||StringUtils.equals(view.getTopicId(), Constant.YANGTZEU_GROUP2)) {
                            ToastUtils.showLong("开发者：官方群无法解散");
                            break;
                        }
                        //群主销毁群
                        instance.dismissGroup(view.getTopicId(), new OnResultStringListener() {
                            @Override
                            public void onResponse(String response) {
                                LogUtils.e(response);
                                ChatInfoBean infoBean = GsonUtils.fromJson(response, ChatInfoBean.class);
                                ToastUtils.showLong("操作：" + infoBean.getMessage());

                                Intent intent = new Intent(Utils.getApp(), ChatActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                                MyUtils.startActivity(intent);
                                activity.finish();
                            }

                            @Override
                            public void onFailure(String error) {
                                ToastUtils.showShort("操作：失败！");
                            }
                        });
                        break;
                    case 200:
                        String message="您真的要退出此群【"+topicInfo.getTopicInfo().getTopicName()+"】吗？";
                        if (StringUtils.equals(view.getTopicId(), Constant.YANGTZEU_GROUP1)||StringUtils.equals(view.getTopicId(), Constant.YANGTZEU_GROUP2)) {
                            ToastUtils.showLong("\n开发者：退出官方群后可在【群组】界面再次加入");
                        }
                        MyUtils.getAlert(activity, message, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //非群主退群
                                instance.quitGroup(view.getTopicId(), new OnResultStringListener() {
                                    @Override
                                    public void onResponse(String response) {
                                        //在我加入的群里面，删除此群
                                        SPUtils.getInstance("group_list").put(topicInfo.getTopicInfo().getTopicId(), false);

                                        ChatInfoBean infoBean = GsonUtils.fromJson(response, ChatInfoBean.class);
                                        ToastUtils.showLong("操作：" + infoBean.getMessage());

                                        Intent intent = new Intent(Utils.getApp(), ChatActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                                        MyUtils.startActivity(intent);
                                        activity.finish();
                                    }

                                    @Override
                                    public void onFailure(String error) {
                                        ToastUtils.showShort("操作：失败！");
                                    }
                                });
                            }
                        }).show();
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public void loadMembersInfo(Activity activity, ChatGroupInfoView view, CreateGroupBean.DataBean dataBean) {
        GroupInnerUserAdapter adapter = view.getAdapter();
        adapter.clear();
        adapter.setData(dataBean);
        adapter.notifyDataSetChanged();
    }


    @Override
    public void loadExtraInfo(final Activity activity, ChatGroupInfoView view, String extra) {
        List<String> url = new ArrayList<>();
        try {
            GroupExtraBean groupExtraBean = GsonUtils.getGson().fromJson(extra, GroupExtraBean.class);
            url = groupExtraBean.getData();
        } catch (Exception e) {
            LogUtils.e("群图片错误");
        }

        if (ObjectUtils.isEmpty(url)) {
            url.add("http://whysroom.oss-cn-beijing.aliyuncs.com/yangtzeu/normal/group_banner.jpg");
        }

        BGABanner banner = view.getBanner();
        banner.setData(url, null);
        banner.setAdapter(new BGABanner.Adapter<ImageView, String>() {
            @Override
            public void fillBannerItem(BGABanner banner, final ImageView itemView, String model, int position) {
                itemView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                Glide.with(activity).asBitmap().load(model).into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        itemView.setImageBitmap(resource);
                    }
                });
            }
        });

    }

    @Override
    public void updateGroupInfo(final Activity activity, final ChatGroupInfoView chatGroupInfoView, final CreateGroupBean.DataBean topicBean, boolean isOnlyBulletin) {
        final UserManager instance = UserManager.getInstance();

        @SuppressLint("InflateParams") final View view = activity.getLayoutInflater().inflate(R.layout.view_input_group_info, null);
        final TextInputEditText inputEditText = view.findViewById(R.id.inputView);
        TextView title = view.findViewById(R.id.title);
        TextView message = view.findViewById(R.id.message);

        if (isOnlyBulletin) {
            title.setText(R.string.update_group_bulletin);
            message.setText(R.string.input_group_bulletin);
            inputEditText.setHint(R.string.input_group_bulletin);
        }

        String s = String.valueOf(topicBean.getTopicInfo().getBulletin());
        inputEditText.setTextSize(15);
        if (!StringUtils.equals(s, "null"))
            inputEditText.setText(s);
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
                .setPositiveButton(R.string.send, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String text = Objects.requireNonNull(inputEditText.getText()).toString().trim();
                        if (!text.isEmpty()) {
                            KeyboardUtils.hideSoftInput(activity);
                            MyUtils.canCloseDialog(dialogInterface, true);
                            ContactGroupBean topicInfo = topicBean.getTopicInfo();
                            instance.updateGroup(chatGroupInfoView.getTopicId(), instance.getAccount(), topicInfo.getTopicName(), text, new OnResultStringListener() {
                                @Override
                                public void onResponse(String response) {
                                    LogUtils.e(response);
                                    ChatInfoBean infoBean = GsonUtils.fromJson(response, ChatInfoBean.class);
                                    ToastUtils.showLong("操作：" + infoBean.getMessage());
                                    loadGroupInfo(activity, chatGroupInfoView);
                                }

                                @Override
                                public void onFailure(String error) {
                                    ToastUtils.showShort("操作：失败！");
                                }
                            });
                        } else {
                            MyUtils.canCloseDialog(dialogInterface, false);
                            ToastUtils.showShort(R.string.input_group_bulletin);
                        }
                    }
                }).create();
        dialog.show();
    }


    @Override
    public void inviteJoinGroup(final Activity activity, final ChatGroupInfoView chatView) {
        @SuppressLint("InflateParams") final View view = LayoutInflater.from(activity).inflate(R.layout.view_input_group, null);
        final TextInputEditText numberView = view.findViewById(R.id.numberView);
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setNegativeButton(activity.getString(R.string.clear), null);

        builder.setPositiveButton(activity.getString(R.string.done), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String id = Objects.requireNonNull(numberView.getText()).toString().trim();
                if (StringUtils.isEmpty(id)) {
                    ToastUtils.showShort(R.string.input_number);
                } else {
                    instance.joinGroup(chatView.getTopicId(), id, new OnResultStringListener() {
                        @Override
                        public void onResponse(String response) {
                            LogUtils.e(response);
                            ChatInfoBean infoBean = GsonUtils.fromJson(response, ChatInfoBean.class);
                            int code = infoBean.getCode();
                            if (code != 200) {
                                ToastUtils.showLong(infoBean.getMessage());
                            } else {
                                ToastUtils.showLong("操作：" + infoBean.getMessage());
                                if (ChatFragment3.presenter != null) {
                                    ChatFragment3.presenter.loadContacts();
                                }
                            }
                        }
                        @Override
                        public void onFailure(String error) {
                            ToastUtils.showShort("操作：失败！");
                        }
                    });
                }
            }
        });
        builder.setView(view);

        final AlertDialog dialog = builder.create();
        dialog.show();
    }

}
