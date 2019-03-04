package com.yangtzeu.model;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;

import com.blankj.utilcode.util.FileIOUtils;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.lib.chat.bean.ContactUserBean;
import com.lib.chat.common.UserManager;
import com.yangtzeu.model.imodel.IChatPart2Model;
import com.yangtzeu.ui.view.ChatPartView2;
import com.yangtzeu.utils.MyUtils;

import java.io.File;
import java.util.List;

public class ChatPart2Model implements IChatPart2Model {
    @Override
    public void loadContacts(Activity activity, final ChatPartView2 view) {
        final List<ContactUserBean> allContacts = UserManager.getAllContactsUser();
        if (ObjectUtils.isNotEmpty(allContacts)) {
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    view.getRefresh().setRefreshing(false);
                    view.getAdapter().setData(allContacts);
                    view.getAdapter().notifyDataSetChanged();
                }
            }, 100);
        } else {
            ToastUtils.showShort("未保存联系人");
        }
    }

    @Override
    public void addAdmin(Activity activity, ChatPartView2 view) {
        String admin = "{\"name\":\"长大助手-小编\",\"note\":\"我是管理员，有问题找我吧~\",\"number\":\"201603246\",\"qq\":\"1223414335\",\"time\":\"1999-02-14\"}";
        String path = MyUtils.rootPath() + "A_Tool/Contact/MIMC/admin.user";
        FileUtils.createOrExistsFile(path);
        FileIOUtils.writeFileFromString(new File(path), admin, false);
    }
}
