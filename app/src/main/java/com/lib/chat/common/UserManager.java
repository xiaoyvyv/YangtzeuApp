package com.lib.chat.common;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.blankj.utilcode.util.CacheDiskUtils;
import com.blankj.utilcode.util.FileIOUtils;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.JsonSyntaxException;
import com.lib.chat.bean.ContactExtraBean;
import com.lib.chat.bean.ContactGroupBean;
import com.lib.chat.bean.ContactUserBean;
import com.lib.chat.bean.CreateGroupBean;
import com.lib.chat.bean.MessageReadBean;
import com.lib.chat.bean.MessagesBean;
import com.lib.chat.listener.handler.MessageHandler;
import com.lib.chat.listener.receive.OnReceiveGroupMessageListener;
import com.lib.chat.listener.receive.OnReceiveMessageListener;
import com.lib.chat.listener.receive.OnReceiveServerMessageListener;
import com.lib.chat.listener.receive.OnReceiveUnlimitGroupMessageListener;
import com.lib.chat.listener.send.OnSendMessageListener;
import com.lib.chat.listener.OnlineStatusListener;
import com.lib.chat.listener.handler.UnlimitedGroupHandler;
import com.lib.subutil.GsonUtils;
import com.lib.yun.Base64;
import com.xiaomi.mimc.MIMCGroupMessage;
import com.xiaomi.mimc.MIMCMessage;
import com.xiaomi.mimc.MIMCUser;
import com.xiaomi.mimc.common.MIMCConstant;
import com.xiaomi.mimc.example.TokenFetcher;
import com.yangtzeu.R;
import com.yangtzeu.database.DatabaseUtils;
import com.yangtzeu.database.MyOpenHelper;
import com.yangtzeu.http.OkHttp;
import com.yangtzeu.http.OnResultStringListener;
import com.yangtzeu.listener.OnResultListener;
import com.yangtzeu.utils.MyUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

public class UserManager {
    /**
     * appId/appKey/appSecret，小米开放平台(https://dev.mi.com/console/appservice/mimc.html)申请
     * 其中appKey和appSecret不可存储于APP端，应存储于APP自己的服务器，以防泄漏。
     * <p>
     * 此处appId/appKey/appSec为小米MimcDemo所有，会在一定时间后失效，建议开发者自行申请
     **/
    // online
    private static final long appId = 2882303761517953315L;
    private static final String appKey = "5691795366315";
    private static final String appSecret = "LL8Lj2Wk429AsZMJmxT8XA==";
    private static final String regionKey = "REGION_CN";
    private static final String domain = "https://mimc.chat.xiaomi.net/";

    // 用户登录APP的帐号
    private String appAccount = "";
    private String url;
    private MIMCUser mUser;
    private MIMCConstant.OnlineStatus mStatus;
    private static UserManager instance;

    //接收私聊消息监听
    private OnReceiveMessageListener messageListener;
    //接收群消息监听
    private OnReceiveGroupMessageListener groupMessageListener;
    //接收无限大群消息监听
    private OnReceiveUnlimitGroupMessageListener unlimitGroupMessageListener;
    //服务器接收到消息回调监听
    private OnReceiveServerMessageListener serverMessageListener;


    //取得实例
    public static UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }
        return instance;
    }



    /**
     * 获取用户帐号
     *
     * @return 成功返回用户帐号，失败返回""
     */
    public String getAccount() {
        return appAccount;
    }

    /**
     * 获取用户在线状态
     *
     * @return STATUS_LOGIN_SUCCESS 在线，STATUS_LOGOUT 下线，STATUS_LOGIN_FAIL 登录失败
     */
    public MIMCConstant.OnlineStatus getStatus() {
        return mStatus;
    }


    /**
     * 获取用户
     *
     * @return 返回已创建用户
     */
    public MIMCUser getUser() {
        return mUser;
    }

    /**
     * 创建用户
     *
     * @param appAccount APP自己维护的用户帐号，不能为null
     * @return 返回新创建的用户
     */
    public MIMCUser newUser(String appAccount) {
        if (StringUtils.isEmpty(appAccount)) {
            appAccount = SPUtils.getInstance("user_info").getString("number");
        }
        if (this.appAccount.equals(appAccount)) return getUser();

        // 若是新用户，先释放老用户资源
        if (getUser() != null) {
            getUser().logout();
            getUser().destroy();
        }

        String cache_path = MyUtils.createSDCardDir("A_Tool/Cache/MiMc/");

        //创建新用户对象
        mUser = MIMCUser.newInstance(appId, appAccount, cache_path);

        //安全认证
        mUser.registerTokenFetcher(new TokenFetcher(appId, appKey, appSecret, domain + "api/account/token", appAccount, regionKey));

        //当前用户在线状态监听
        mUser.registerOnlineStatusListener(new OnlineStatusListener() {
            @Override
            public void statusChange(MIMCConstant.OnlineStatus status, String errType, String errReason, String errDescription) {
                mStatus = status;
                super.statusChange(status, errType, errReason, errDescription);
            }
        });

        //注册私聊-群聊消息接收监听
        registerMessageHandler(mUser);


        //无限大群非消息相关信息回调（创建/加入/离开/解散）
        mUser.registerUnlimitedGroupHandler(new UnlimitedGroupHandler());

        //当前在线用户ID
        this.appAccount = appAccount;

        return mUser;
    }


    /**
     * 注册私聊-群聊消息接收监听
     *
     * @param mUser 当前用户
     */
    private void registerMessageHandler(MIMCUser mUser) {
        if (mUser != null)
            mUser.registerMessageHandler(new MessageHandler(messageListener, groupMessageListener, unlimitGroupMessageListener, serverMessageListener));
    }

    /**
     * 设置接收私聊消息监听，刷新注册
     *
     * @param messageListener 私聊消息监听
     */
    public void setMessageListener(OnReceiveMessageListener messageListener) {
        this.messageListener = messageListener;
        registerMessageHandler(mUser);
    }

    /**
     * 设置接收群聊消息监听，刷新注册
     *
     * @param groupMessageListener 群聊消息监听
     */
    public void setGroupMessageListener(OnReceiveGroupMessageListener groupMessageListener) {
        this.groupMessageListener = groupMessageListener;
        registerMessageHandler(mUser);
    }

    /**
     * 设置接收无限大群聊消息监听，刷新注册
     *
     * @param unlimitGroupMessageListener 无限大群聊消息监听
     */
    public void setUnlimitGroupMessageListener(OnReceiveUnlimitGroupMessageListener unlimitGroupMessageListener) {
        this.unlimitGroupMessageListener = unlimitGroupMessageListener;
        registerMessageHandler(mUser);
    }

    /**
     * 设置接服务器收到消息监听，刷新注册
     *
     * @param serverMessageListener 无限大群聊消息监听
     */
    public void setServerMessageListener(OnReceiveServerMessageListener serverMessageListener) {
        this.serverMessageListener = serverMessageListener;
        registerMessageHandler(mUser);
    }

    /**
     * 四种消息监听，同时设置
     *
     * @param messageListener             私聊
     * @param groupMessageListener        群聊
     * @param unlimitGroupMessageListener 大群聊
     * @param serverMessageListener       服务器
     */
    public void setAllMessageListener(OnReceiveMessageListener messageListener, OnReceiveGroupMessageListener groupMessageListener
            , OnReceiveUnlimitGroupMessageListener unlimitGroupMessageListener, OnReceiveServerMessageListener serverMessageListener) {
        setMessageListener(messageListener);
        setGroupMessageListener(groupMessageListener);
        setUnlimitGroupMessageListener(unlimitGroupMessageListener);
        setServerMessageListener(serverMessageListener);
    }

    /**
     * 发送私聊消息
     *
     * @param toAppAccount 对方ID
     * @param payload      消息
     * @param bizType      消息类型
     */
    public void sendMsg(String toAppAccount, byte[] payload, String bizType, OnSendMessageListener<MIMCMessage> onSendMessageListener) {
        if (StringUtils.equals(appAccount, toAppAccount)) {
            onSendMessageListener.onFailure("无法给自己发送消息");
            return;
        }
        String message_id = mUser.sendMessage(toAppAccount, payload, bizType);

        //设置服务器接收消息状态监听
        setServerMessageListener(onSendMessageListener);

        MIMCMessage mimcMessage = new MIMCMessage(message_id, 0, appAccount, null, toAppAccount, null, payload, System.currentTimeMillis(), bizType);
        if (StringUtils.isEmpty(message_id)) {
            onSendMessageListener.onFailure("消息列队异常");
        } else {
            onSendMessageListener.onSending(mimcMessage);
        }

        //显示私人会话列表
        UserManager.getInstance().setContactVisibility(toAppAccount, Constant.NO_DELETE);
    }


    /**
     * 发送群聊或者无限大聊消息
     *
     * @param groupID          群ID
     * @param content          消息
     * @param bizType          消息类型
     * @param isUnlimitedGroup 是否为无限大群
     */
    public void sendGroupMsg(long groupID, byte[] content, String bizType, boolean isUnlimitedGroup, OnSendMessageListener<MIMCGroupMessage> onSendMessageListener) {

        String message_id;
        if (isUnlimitedGroup) {
            message_id = mUser.sendUnlimitedGroupMessage(groupID, content, bizType);
        } else {
            message_id = mUser.sendGroupMessage(groupID, content, bizType);
        }

        //设置服务器接收消息状态监听
        setServerMessageListener(onSendMessageListener);
        MIMCGroupMessage groupMessage = new MIMCGroupMessage(message_id, 0, appAccount, null, 0, content, System.currentTimeMillis(), bizType);

        if (StringUtils.isEmpty(message_id)) {
            onSendMessageListener.onFailure("消息列队异常");
        } else {
            onSendMessageListener.onSending(groupMessage);
        }

        //显示群聊会话列表
        UserManager.getInstance().setGroupContactVisibility(String.valueOf(groupID), Constant.NO_DELETE);
    }


    /**
     * 创建群
     *
     * @param groupName 群名
     * @param users     群成员，多个成员之间用英文逗号(,)分隔
     */
    public void createGroup(final String groupName, final String users, OnResultStringListener onResultStringListener) {
        url = domain + "api/topic/" + appId;
        String json = "{\"topicName\":\"" + groupName + "\", \"accounts\":\"" + users + ",201602810\"}";
        MediaType JSON = MediaType.parse("application/json");
        Request request = new Request
                .Builder()
                .url(url)
                .addHeader("token", mUser.getToken())
                .post(RequestBody.create(JSON, json))
                .build();

        OkHttp.do_Post(request, onResultStringListener);
    }


    /**
     * 查询指定群信息
     *
     * @param groupId 群ID
     */
    public void queryGroupInfo(final String groupId, OnResultStringListener onResultStringListener) {
        url = domain + "api/topic/" + appId + "/" + groupId;
        Request request = new Request
                .Builder()
                .url(url)
                .addHeader("token", mUser.getToken())
                .get()
                .build();

        OkHttp.do_Post(request, onResultStringListener);
    }


    /**
     * 查询所属群信息
     */
    public void queryGroupsOfAccount(OnResultStringListener onResultStringListener) {
        url = domain + "api/topic/" + appId + "/account";
        Request request = new Request
                .Builder()
                .url(url)
                .addHeader("token", mUser.getToken())
                .get()
                .build();

        OkHttp.do_Post(request, onResultStringListener);
    }

    /**
     * 邀请用户加入群
     *
     * @param groupId 群ID
     * @param users   加入成员，多个成员之间用英文逗号(,)分隔
     */
    public void joinGroup(final String groupId, final String users, OnResultStringListener onResultStringListener) {
        url = domain + "api/topic/" + appId + "/" + groupId + "/accounts";
        String json = "{\"accounts\":\"" + users + "\"}";
        LogUtils.e(json);

        MediaType JSON = MediaType.parse("application/json");
        Request request = new Request
                .Builder()
                .url(url)
                .addHeader("token", mUser.getToken())
                .post(RequestBody.create(JSON, json))
                .build();

        OkHttp.do_Post(request, onResultStringListener);
    }

    /**
     * 非群主成员退群
     *
     * @param groupId 群ID
     */
    public void quitGroup(final String groupId, OnResultStringListener onResultStringListener) {
        url = domain + "api/topic/" + appId + "/" + groupId + "/account";
        Request request = new Request
                .Builder()
                .url(url)
                .addHeader("token", mUser.getToken())
                .delete()
                .build();

        OkHttp.do_Post(request, onResultStringListener);
    }

    /**
     * 群主踢成员出群
     *
     * @param groupId 群ID
     * @param users   群成员，多个成员之间用英文逗号(,)分隔
     */
    public void kickGroup(final String groupId, final String users, OnResultStringListener onResultStringListener) {
        url = domain + "api/topic/" + appId + "/" + groupId + "/accounts?accounts=" + users;
        Request request = new Request
                .Builder()
                .url(url)
                .addHeader("token", mUser.getToken())
                .delete()
                .build();

        OkHttp.do_Post(request, onResultStringListener);
    }

    /**
     * 群主更新群信息
     *
     * @param groupId          群ID
     * @param newOwnerAccount  若为群成员则指派新的群主
     * @param newGroupName     群名
     * @param newGroupBulletin 群公告
     */
    public void updateGroup(final String groupId, final String newOwnerAccount, final String newGroupName, final String newGroupBulletin, OnResultStringListener onResultStringListener) {
        url = domain + "api/topic/" + appId + "/" + groupId;
        // 注意：不指定的信息则不更新（键值对一起不指定）
        String json = "{\"ownerAccount\":\"" + newOwnerAccount + "\",\"topicName\":\"" + newGroupName + "\",\"bulletin\":\"" + newGroupBulletin + "\"}";
        MediaType JSON = MediaType.parse("application/json");
        Request request = new Request
                .Builder()
                .url(url)
                .addHeader("token", mUser.getToken())
                .put(RequestBody.create(JSON, json))
                .build();

        OkHttp.do_Post(request, onResultStringListener);
    }

    /**
     * 群主销毁群
     *
     * @param groupId 群ID
     */
    public void dismissGroup(final String groupId, OnResultStringListener onResultStringListener) {
        url = domain + "api/topic/" + appId + "/" + groupId;
        Request request = new Request
                .Builder()
                .url(url)
                .addHeader("token", mUser.getToken())
                .delete()
                .build();

        OkHttp.do_Post(request, onResultStringListener);
    }

    /**
     * 拉取单聊消息记录
     *
     * @param toAccount   接收方帐号
     * @param fromAccount 发送方帐号
     * @param utcToTime   指定时间向前
     * @param count       条数
     */
    public void pullP2PHistory(String toAccount, String fromAccount, String utcToTime, String count, OnResultStringListener onResultStringListener) {
        url = domain + "api/msg/p2p/queryOnCount/";
        String json = "{\"toAccount\":\"" + toAccount + "\", \"fromAccount\":\""
                + fromAccount + "\", \"utcToTime\":\"" + utcToTime + "\", \"count\":\"" +
                count + "\"}";
        MediaType JSON = MediaType.parse("application/json;charset=UTF-8");
        Request request = new Request
                .Builder()
                .url(url)
                .addHeader("Accept", "application/json;charset=UTF-8")
                .addHeader("token", mUser.getToken())
                .post(RequestBody.create(JSON, json))
                .build();

        OkHttp.do_Post(request, onResultStringListener);
    }


    /**
     * 拉取群聊消息记录
     *
     * @param account   拉取者帐号
     * @param topicId   群ID
     * @param utcToTime 指定开始时间之前
     * @param count     条数
     */
    public void pullP2THistory(String account, String topicId, String utcToTime, String count, OnResultStringListener onResultStringListener) {
        url = domain + "api/msg/p2t/queryOnCount/";
        String json = "{\"account\":\"" + account + "\", \"topicId\":\""
                + topicId + "\", \"utcToTime\":\"" + utcToTime + "\", \"count\":\"" + count + "\"}";
        MediaType JSON = MediaType.parse("application/json;charset=UTF-8");
        Request request = new Request
                .Builder()
                .url(url)
                .addHeader("Accept", "application/json;charset=UTF-8")
                .addHeader("token", mUser.getToken())
                .post(RequestBody.create(JSON, json))
                .build();

        OkHttp.do_Post(request, onResultStringListener);
    }

    /**
     * 查询无限大群成员
     *
     * @param topicId 群ID
     */
    public void queryUnlimitedGroupMembers(long topicId, OnResultStringListener onResultStringListener) {
        url = domain + "/api/uctopic/userlist";
        final Request request = new Request
                .Builder()
                .url(url)
                .addHeader("token", mUser.getToken())
                .addHeader("topicId", String.valueOf(topicId))
                .get()
                .build();

        OkHttp.do_Post(request, onResultStringListener);
    }

    /**
     * 查询无限大群
     */
    public void queryUnlimitedGroups(OnResultStringListener onResultStringListener) {
        String url = domain + "/api/uctopic/topics";
        final Request request = new Request
                .Builder()
                .url(url)
                .addHeader("token", mUser.getToken())
                .get()
                .build();

        OkHttp.do_Post(request, onResultStringListener);
    }

    /**
     * 查询无限大群在线用户数
     *
     * @param topicId
     */
    public void queryUnlimitedGroupOnlineUsers(long topicId, OnResultStringListener onResultStringListener) {
        url = domain + "/api/uctopic/onlineinfo";
        final Request request = new Request
                .Builder()
                .url(url)
                .addHeader("token", mUser.getToken())
                .addHeader("topicId", String.valueOf(topicId))
                .get()
                .build();

        OkHttp.do_Post(request, onResultStringListener);
    }

    /**
     * 查询回话列表
     */
    public void queryContactInfo(OnResultStringListener onResultStringListener) {
        url = domain + "api/contact/";
        final Request request = new Request
                .Builder()
                .url(url)
                .addHeader("token", mUser.getToken())
                .get()
                .build();

        OkHttp.do_Post(request, onResultStringListener);
    }


    /**
     * 更新单聊会话扩展
     */
    public void updateContactExtra(String account, String extra, OnResultStringListener onResultStringListener) {
        url = domain + "api/contact/p2p/extra/update";
        String json = "{\"account\":\"" + account + "\", \"extra\":\"" + extra + "\"}";
        MediaType JSON = MediaType.parse("application/json;charset=UTF-8");
        Request request = new Request
                .Builder()
                .url(url)
                .addHeader("Accept", "application/json;charset=UTF-8")
                .addHeader("token", mUser.getToken())
                .post(RequestBody.create(JSON, json))
                .build();

        OkHttp.do_Post(request, onResultStringListener);
    }

    /**
     * 更新群聊会话扩展
     */
    public void updateGroupContactExtra(String topicId, String extra, OnResultStringListener onResultStringListener) {
        url = domain + "api/contact/p2t/extra/update";
        String json = "{\"topicId\":\"" + topicId + "\", \"extra\":\"" + extra + "\"}";
        MediaType JSON = MediaType.parse("application/json;charset=UTF-8");
        Request request = new Request
                .Builder()
                .url(url)
                .addHeader("Accept", "application/json;charset=UTF-8")
                .addHeader("token", mUser.getToken())
                .post(RequestBody.create(JSON, json))
                .build();

        OkHttp.do_Post(request, onResultStringListener);
    }


    /**
     * 设置未读
     *
     * @param id         对象id
     * @param unReadSize 未读数目
     */
    public void setUnRead(String id, long unReadSize) {
        MyOpenHelper helper = DatabaseUtils.getHelper("read.db");
        if (helper.isTableExists(MessageReadBean.class)) {
            MessageReadBean messageReadBean = helper.queryById(MessageReadBean.class, id);
            if (messageReadBean != null) {
                //原本未读数目
                long oldUnReadSize = messageReadBean.getUnReadSize();
                //新的未读数目
                long newUnReadSize = oldUnReadSize + unReadSize;
                //更新未读数目
                messageReadBean.setUnReadSize(newUnReadSize);
            } else {
                messageReadBean = new MessageReadBean();
                messageReadBean.setId(Long.parseLong(id));
                messageReadBean.setUnReadSize(unReadSize);
            }
            helper.save(messageReadBean);

            if (id.length() < 10) {
                //显示私人会话列表
                UserManager.getInstance().setContactVisibility(id, Constant.NO_DELETE);
            } else {
                //显示群聊会话列表
                UserManager.getInstance().setGroupContactVisibility(id, Constant.NO_DELETE);
            }
        } else {
            helper.createTableIfNotExists(MessageReadBean.class);
            setUnRead(id, unReadSize);
        }

    }


    /**
     * 取得未读数目
     *
     * @param id 对象id
     * @return 未读数目
     */
    public long getUnRead(String id) {
        MyOpenHelper helper = DatabaseUtils.getHelper("read.db");
        if (helper.isTableExists(MessageReadBean.class)) {
            MessageReadBean messageReadBean = helper.queryById(MessageReadBean.class, id);
            if (messageReadBean != null) {
                return messageReadBean.getUnReadSize();
            } else {
                return 0;
            }
        } else {
            helper.createTableIfNotExists(MessageReadBean.class);
            return getUnRead(id);
        }
    }

    /**
     * 取得所有未读数目
     *
     * @return 未读数目
     */
    public long getAllUnRead() {
        long unRead = 0;
        //统计未读总数
        MyOpenHelper helper = DatabaseUtils.getHelper("read.db");
        List<MessageReadBean> messageReadBeans = helper.queryAll(MessageReadBean.class);
        if (ObjectUtils.isNotEmpty(messageReadBeans)) {
            for (MessageReadBean messageReadBean : messageReadBeans) {
                unRead = unRead + messageReadBean.getUnReadSize();
            }
        }
        return unRead;
    }

    /**
     * 清空未读
     *
     * @param id 对象id
     */
    public void setUnReadNone(String id) {
        MyOpenHelper helper = DatabaseUtils.getHelper("read.db");
        if (helper.isTableExists(MessageReadBean.class)) {
            MessageReadBean messageReadBean = new MessageReadBean();
            messageReadBean.setId(Long.parseLong(id));
            messageReadBean.setUnReadSize(0);
            helper.save(messageReadBean);
        } else {
            helper.createTableIfNotExists(MessageReadBean.class);
            setUnReadNone(id);
        }
    }

    /**
     * 删除/显示私聊会话 （隐藏）
     *
     * @param id                     会话id
     * @param extra                  扩展
     */
    public void setContactVisibility(String id, String extra) {
        updateContactExtra(id, extra, new OnResultStringListener() {
            @Override
            public void onResponse(String response) {
                ContactExtraBean contactBean = GsonUtils.fromJson(response, ContactExtraBean.class);

                LogUtils.i(GsonUtils.toJson(contactBean));
            }

            @Override
            public void onFailure(String error) {
                LogUtils.e(error);
            }
        });
    }

    /**
     * 删除/显示群聊会话 （隐藏）
     *
     * @param id                     会话id
     * @param extra                  扩展
     */
    public void setGroupContactVisibility(String id, String extra) {
        updateGroupContactExtra(id, extra, new OnResultStringListener() {
            @Override
            public void onResponse(String response) {
                ContactExtraBean contactBean = GsonUtils.fromJson(response, ContactExtraBean.class);
                LogUtils.i(GsonUtils.toJson(contactBean));
            }

            @Override
            public void onFailure(String error) {
                LogUtils.e(error);
            }
        });
    }


    /**
     * 输入学号进行会话
     */
    public static void showChatNumberDialog(final Context context) {
        @SuppressLint("InflateParams")
        View view = LayoutInflater.from(context).inflate(R.layout.view_input_number, null);
        final TextInputEditText numberView = view.findViewById(R.id.numberView);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setNegativeButton(context.getString(R.string.clear), null);
        builder.setPositiveButton(context.getString(R.string.done), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String text = Objects.requireNonNull(numberView.getText()).toString().trim();
                if (StringUtils.isEmpty(text)) {
                    ToastUtils.showShort(R.string.input_number);
                } else if (StringUtils.equals(text, UserManager.getInstance().getAccount())) {
                    ToastUtils.showShort(context.getString(R.string.cannt_chat_myself));
                } else {
                    MyUtils.chatOnline(context, text, Constant.USER_TYPE_USER);
                    ContactUserBean bean = new ContactUserBean();
                    bean.setName("用户：" + text);
                    bean.setNumber(text);
                    boolean b = UserManager.saveContacts(bean);
                    if (b) {
                        LogUtils.e("联系人添加成功");
                    } else {
                        LogUtils.e("联系人添加失败");
                    }
                }
            }
        });
        builder.setView(view);

        final AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * 保存和更新单个联系人
     *
     * @param contactUserBean 联系人
     */
    public  static boolean saveContacts(@NonNull ContactUserBean contactUserBean) {
        String savePath = MyUtils.rootPath() + "A_Tool/Contact/MIMC/" + contactUserBean.getNumber() + ".user";
        String friend = GsonUtils.toJson(contactUserBean);
        return FileIOUtils.writeFileFromString(new File(savePath), friend, false);
    }
    /**
     * 取得单个联系人信息
     *
     * @param number 联系人学号
     */
    public static ContactUserBean getContacts(String number) {
        File file = new File(MyUtils.rootPath() + "A_Tool/Contact/MIMC/" + number + ".user");
        if (FileUtils.isFileExists(file)) {
            try {
                String string = FileIOUtils.readFile2String(file, "utf-8");
                return GsonUtils.getGson().fromJson(string, ContactUserBean.class);
            } catch (Exception e) {
                return null;
            }
        } else return null;
    }

    /**
     * 取得所有联系人信息
     */
    public static List<ContactUserBean> getAllContactsUser() {
        List<ContactUserBean> contactUserBeans = new ArrayList<>();
        String savePath = MyUtils.createSDCardDir("A_Tool/Contact/MIMC/");
        List<File> files = FileUtils.listFilesInDir(savePath);
        if (ObjectUtils.isNotEmpty(files)) {
            Collections.reverse(files);
            for (File file : files) {
                try {
                    String json = FileIOUtils.readFile2String(file);
                    ContactUserBean bean = GsonUtils.getGson().fromJson(json, ContactUserBean.class);
                    contactUserBeans.add(bean);
                } catch (Exception ignored) {
                }
            }
        }
        return contactUserBeans;
    }

    /**
     * 取得所有官方群信息
     */
    public static List<ContactGroupBean> getAllContactsGroup() {
        List<ContactGroupBean> beanList = new ArrayList<>();
        String savePath = MyUtils.createSDCardDir("A_Tool/Contact/MIMC_GROUP/");
        List<File> files = FileUtils.listFilesInDir(savePath);
        if (ObjectUtils.isNotEmpty(files)) {
            Collections.reverse(files);
            for (File file : files) {
                try {
                    String json = FileIOUtils.readFile2String(file);
                    ContactGroupBean bean = GsonUtils.getGson().fromJson(json, ContactGroupBean.class);
                    beanList.add(bean);
                } catch (Exception ignored) {
                }
            }
        }
        return beanList;
    }


    public MessagesBean pToMessagesBean(MIMCMessage mimcMessage) {
        byte[] bytes = mimcMessage.getPayload();
        MessagesBean messagesBean = new MessagesBean();
        messagesBean.setPayload(new String(Base64.encode(bytes, Base64.DEFAULT)));

        messagesBean.setBizType(mimcMessage.getBizType());
        messagesBean.setFromAccount(mimcMessage.getFromAccount());
        messagesBean.setToAccount(mimcMessage.getToAccount());
        messagesBean.setTs(mimcMessage.getTimestamp());
        messagesBean.setSequence(String.valueOf(mimcMessage.getSequence()));
        return messagesBean;
    }

    public MessagesBean tToMessagesBean(MIMCGroupMessage mimcGroupMessage) {
        byte[] bytes = mimcGroupMessage.getPayload();
        MessagesBean messagesBean = new MessagesBean();
        messagesBean.setPayload(new String(Base64.encode(bytes, Base64.DEFAULT)));

        messagesBean.setBizType(mimcGroupMessage.getBizType());
        messagesBean.setFromAccount(mimcGroupMessage.getFromAccount());
        messagesBean.setToAccount(String.valueOf(mimcGroupMessage.getTopicId()));
        messagesBean.setTs(mimcGroupMessage.getTimestamp());
        messagesBean.setSequence(String.valueOf(mimcGroupMessage.getSequence()));
        return messagesBean;
    }
}