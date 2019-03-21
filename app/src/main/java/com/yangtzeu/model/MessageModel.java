package com.yangtzeu.model;

import android.app.Activity;

import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.yangtzeu.R;
import com.yangtzeu.entity.MessageBean;
import com.yangtzeu.http.OkHttp;
import com.yangtzeu.http.OnResultStringListener;
import com.yangtzeu.model.imodel.IMessageModel;
import com.yangtzeu.ui.view.MessageView;
import com.yangtzeu.url.Url;

public class MessageModel implements IMessageModel {

    @Override
    public void loadMessageData(final Activity activity, final MessageView view) {
        final String number = SPUtils.getInstance("user_info").getString("number");
        String url = Url.getMessage(number);
        OkHttp.do_Get(url, new OnResultStringListener() {
            @Override
            public void onResponse(String response) {
                view.getData().clear();
                view.getAdapter().clear();
                view.getAdapter().notifyDataSetChanged();
                final MessageBean bean = (new Gson()).fromJson(response, MessageBean.class);
                String info = bean.getInfo();
                if (info.contains("查询成功")) {
                    if (ObjectUtils.isNotEmpty(bean.getData())) {
                        view.getData().addAll(bean.getData());
                        view.getAdapter().setData( view.getData());
                        view.getAdapter().notifyItemRangeChanged(0, view.getAdapter().getItemCount());
                    } else {
                        ToastUtils.showShort(R.string.no_data);
                    }
                } else {
                    ToastUtils.showShort(info);
                }
                view.getRefresh().finishRefresh();

            }

            @Override
            public void onFailure(String error) {
                view.getRefresh().finishRefresh();
                ToastUtils.showShort(R.string.try_again);
            }
        });
    }
}
