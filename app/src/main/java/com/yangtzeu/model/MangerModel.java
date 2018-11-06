package com.yangtzeu.model;

import android.app.Activity;
import android.widget.FrameLayout;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.lib.subutil.GsonUtils;
import com.yangtzeu.R;
import com.yangtzeu.entity.FeedBackBean;
import com.yangtzeu.entity.LoveBean;
import com.yangtzeu.http.OkHttp;
import com.yangtzeu.http.OnResultStringListener;
import com.yangtzeu.model.imodel.IMangerModel;
import com.yangtzeu.ui.view.MangerView;
import com.yangtzeu.url.Url;

import java.util.List;

public class MangerModel implements IMangerModel {
    @Override
    public void loadFeedBack(Activity activity, final MangerView view) {
        final List<FeedBackBean.DataBean> data = view.getData();
        final int old_index = view.getAdapter().getItemCount();
        OkHttp.do_Get(Url.Yangtzeu_App_FeedBack + "?action=query&page=" + view.getPage(), new OnResultStringListener() {
            @Override
            public void onResponse(String response) {
                view.getRefresh().finishRefresh();
                view.getRefresh().finishLoadMore();

                if (view.isRefresh()) {
                    view.getAdapter().clear();
                    view.getData().clear();
                }

                FeedBackBean loveBean = GsonUtils.fromJson(response, FeedBackBean.class);
                String info = loveBean.getInfo();

                if (info.equals("建议查询成功")) {
                    if (ObjectUtils.isNotEmpty(loveBean.getData())) {
                        data.addAll(loveBean.getData());
                        view.getAdapter().addData(data);
                        view.getAdapter().notifyItemRangeChanged(old_index, view.getAdapter().getItemCount());
                    } else {
                        ToastUtils.showShort(R.string.no_more);
                    }
                } else {
                    ToastUtils.showShort(info);
                }

            }

            @Override
            public void onFailure(String error) {
                view.getRefresh().finishRefresh();
                view.getRefresh().finishLoadMore();
            }

        });
    }
}
