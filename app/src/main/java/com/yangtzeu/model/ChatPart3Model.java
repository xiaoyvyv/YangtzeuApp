package com.yangtzeu.model;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;

import com.blankj.utilcode.util.FileIOUtils;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.lib.chat.bean.ContactGroupBean;
import com.lib.chat.bean.ContactUserBean;
import com.lib.chat.bean.QueryGroupBean;
import com.lib.chat.common.UserManager;
import com.lib.subutil.GsonUtils;
import com.yangtzeu.http.OnResultStringListener;
import com.yangtzeu.model.imodel.IChatPart3Model;
import com.yangtzeu.ui.view.ChatPartView3;
import com.yangtzeu.utils.MyUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ChatPart3Model implements IChatPart3Model {
    @Override
    public void loadContacts(Activity activity, final ChatPartView3 view) {
        view.getAdapter().clear();
        final List<ContactGroupBean> allContacts = new ArrayList<>();
        //final List<ContactGroupBean> allContacts = UserManager.getAllContactsGroup();
        UserManager manager = UserManager.getInstance();
        manager.queryGroupsOfAccount(new OnResultStringListener() {
            @Override
            public void onResponse(String response) {
                view.getRefresh().setRefreshing(false);
                QueryGroupBean queryGroupBean = GsonUtils.fromJson(response, QueryGroupBean.class);
                int code = queryGroupBean.getCode();
                if (code == 200) {
                    List<ContactGroupBean> contactGroupBeans = queryGroupBean.getData();
                    allContacts.addAll(contactGroupBeans);
                    view.getAdapter().setData(allContacts);
                    view.getAdapter().notifyDataSetChanged();
                } else {
                    ToastUtils.showShort(queryGroupBean.getMessage());
                }
            }

            @Override
            public void onFailure(String error) {
                LogUtils.e(error);
            }
        });
    }

    @Override
    public void addAdmin(Activity activity, ChatPartView3 view) {
        String admin = "{\"bulletin\":null,\"extra\":\"请文明聊天，禁止发布不良信息\",\"ownerAccount\":\"201603246\",\"ownerUuid\":\"17521175009951744\",\"topicId\":\"17621516447907840\",\"topicName\":\"长大助手官方群\"}";
        String path = MyUtils.rootPath() + "A_Tool/Contact/MIMC_GROUP/admin.group";
        FileUtils.createOrExistsFile(path);
        FileIOUtils.writeFileFromString(new File(path), admin, false);
    }
}
