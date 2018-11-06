package com.yangtzeu.model;

import android.app.Activity;

import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.lib.subutil.GsonUtils;
import com.yangtzeu.R;
import com.yangtzeu.entity.ShopBean;
import com.yangtzeu.http.OkHttp;
import com.yangtzeu.http.OnResultStringListener;
import com.yangtzeu.model.imodel.IShopModel;
import com.yangtzeu.ui.view.ShopView;
import com.yangtzeu.url.Url;

import java.util.List;

public class ShopModel implements IShopModel {
    @Override
    public void loadData(Activity activity, final ShopView view) {
        final List<ShopBean.DataBean> data = view.getData();
        String url = Url.queryAllGoods(view.getType(), view.getText(), view.getStart());
        final int old_index = view.getAdapter().getItemCount();
        OkHttp.do_Get(url, new OnResultStringListener() {
            @Override
            public void onResponse(String response) {
                view.getRefresh().finishRefresh();
                view.getRefresh().finishLoadMore();
                if (view.isRefresh()) {
                    view.getData().clear();
                    view.getAdapter().clear();
                }

                ShopBean shopBean = GsonUtils.fromJson(response, ShopBean.class);
                String info = shopBean.getInfo();
                if (info.equals("商品查询成功")) {
                    if (ObjectUtils.isNotEmpty(shopBean.getData())) {
                        data.addAll(shopBean.getData());
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
