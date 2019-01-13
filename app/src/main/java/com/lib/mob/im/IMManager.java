package com.lib.mob.im;

import android.content.Intent;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.Utils;
import com.mob.MobSDK;
import com.mob.imsdk.MobIM;
import com.mob.imsdk.MobIMCallback;
import com.mob.imsdk.model.IMConversation;
import com.mob.imsdk.model.IMGroup;
import com.mob.imsdk.model.IMUser;
import com.yangtzeu.listener.OnResultListener;
import com.yangtzeu.ui.activity.ChatDetailsActivity;
import com.yangtzeu.utils.MyUtils;

public class IMManager {
    private static IMUser user = null;

    public static IMUser getUser() {
        if (user == null) {
            user = getCachedUser();
        }
        return user;
    }

    //获取服务器当前User
    public static void getIMUser(final OnResultListener<IMUser> listener) {
        MobIM.getCurrentIMUser(new MobIMCallback<IMUser>() {
            @Override
            public void onSuccess(IMUser user) {
                if (listener != null) {
                    listener.onResult(user);
                }
            }

            @Override
            public void onError(int i, String s) {
                if (listener != null) {
                    listener.onResult(getCachedUser());
                }
            }
        });
    }

    //获取本地User
    private static IMUser getCachedUser() {
        //姓名
        String number = SPUtils.getInstance("user_info").getString("number", "000000");
        String name = SPUtils.getInstance("user_info").getString("name", "用户：" + number);
        String qq = SPUtils.getInstance("user_info").getString("qq", "default_header");

        IMUser user = new IMUser();
        user.setId(number);
        user.setNickname(name);

        if (qq.equals("default_header")) {
            user.setAvatar(qq);
            MobSDK.setUser(number, name, qq, null);
        } else {
            user.setAvatar(MyUtils.getQQHeader(qq));
            MobSDK.setUser(number, name, MyUtils.getQQHeader(qq), null);
        }
        return user;
    }

    //登录IM
    public static void loginIM() {
        String number = SPUtils.getInstance("user_info").getString("number", "000000");
        String name = SPUtils.getInstance("user_info").getString("name", "用户：" + number);
        String qq = SPUtils.getInstance("user_info").getString("qq", "default_header");

        if (qq.equals("default_header")) {
            MobSDK.setUser(number, name, qq, null);
        } else {
            MobSDK.setUser(number, name, MyUtils.getQQHeader(qq), null);
        }
    }

    //创建群
    public static void createGroup(String[] members) {
        MobIM.getGroupManager().createGroup("新长大助手官方群", "反馈学习交流群，禁止灌水！", members, new MobIMCallback<IMGroup>() {
            @Override
            public void onSuccess(IMGroup imGroup) {
                String id = imGroup.getId();
                String name = imGroup.getName();
                String desc = imGroup.getDesc();
                String createTime = TimeUtils.millis2String(imGroup.getCreateTime());
                LogUtils.e(id, name, desc, createTime);
                ToastUtils.showShort("创建成功：" + id);
            }

            @Override
            public void onError(int i, String s) {
                LogUtils.e("创建失败：" + s);
                ToastUtils.showShort("创建失败");
            }
        });
    }

    //加群
    public static void joinGroup(final String groupId) {
        MobIM.getGroupManager().joinGroup(groupId, new MobIMCallback<IMGroup>() {
            @Override
            public void onSuccess(IMGroup imGroup) {
                IMManager.chat(groupId, IMConversation.TYPE_GROUP);
            }

            @Override
            public void onError(int i, String s) {
                if (s.contains("5110215")) {
                    IMManager.chat(groupId, IMConversation.TYPE_GROUP);
                } else {
                    ToastUtils.showShort(s);
                }
            }
        });
    }

    //打开聊天
    public static void chat(String id, int IMConversationTYPE) {
        //targetId - 目标id（群聊为群的id，私聊为对方id）
        Intent intent = new Intent(Utils.getApp(), ChatDetailsActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("type", IMConversationTYPE);
        MyUtils.startActivity(intent);
    }
}
