package com.yangtzeu.model;

import android.app.Activity;

import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.lib.subutil.GsonUtils;
import com.yangtzeu.R;
import com.yangtzeu.entity.LoveBean;
import com.yangtzeu.http.OkHttp;
import com.yangtzeu.http.OnResultStringListener;
import com.yangtzeu.model.imodel.ILoveModel;
import com.yangtzeu.ui.view.LoveView;
import com.yangtzeu.url.Url;

import java.util.List;

public class LoveModel implements ILoveModel {

    @Override
    public void loadData(Activity activity, final LoveView view) {
        final List<LoveBean.DataBean> data = view.getData();
        String url = Url.queryAllLove(view.getType(),view.getText(),view.getStart());
        final int old_index = view.getAdapter().getItemCount();
        OkHttp.do_Get(url, new OnResultStringListener() {
            @Override
            public void onResponse(String response) {
                view.getRefresh().finishRefresh();
                view.getRefresh().finishLoadMore();

                if (view.isRefresh()) {
                    data.clear();
                    view.getAdapter().clear();
                }

                LoveBean loveBean = GsonUtils.fromJson(response, LoveBean.class);
                String info = loveBean.getInfo();
                if (info.equals("表白查询成功")) {
                    if (ObjectUtils.isNotEmpty(loveBean.getData())) {
                        data.addAll(loveBean.getData());
                        view.getAdapter().setData(data);
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
                ToastUtils.showShort(R.string.try_again);
            }
        });

    }
}
